package io.moyada.sharingan.infrastructure.module;


import io.moyada.sharingan.infrastructure.enums.PrimitiveClass;
import io.moyada.sharingan.infrastructure.util.SoftReferenceUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态状态依赖
 * @author xueyikang
 * @create 2018-04-27 16:33
 */
@DependsOn("mavenConfig")
@Component
public class ModuleFetch extends DefaultFetch implements ApplicationContextAware, MetadataFetch {

    private static final int CACHE_TIME = 720000;

    private ApplicationContext applicationContext;

    // 类加载器映射
    private final Map<Dependency, SoftReference<AppClassLoader>> loaderMap = new HashMap<>();

    private ArtifactFetch artifactFetch;

    @Autowired
    public ModuleFetch(ArtifactFetch artifactFetch) {
        this.artifactFetch = artifactFetch;
    }

    @Override
    public Class getClass(Dependency dependency, String className) throws ClassNotFoundException {
        Class<?> clazz = PrimitiveClass.forName(className);
        if (null != clazz) {
            return clazz;
        }

        ModuleClassLoader classLoader = getClassLoader(dependency);
        if(null == classLoader) {
            throw new ClassNotFoundException("Load " + className + " failed.");
        }
        clazz = classLoader.loadLocalClass(className);
        return clazz;
    }

    /**
     * 获取对应依赖类加载器
     * @param dependency
     * @return
     */
    @Override
    public ModuleClassLoader getClassLoader(Dependency dependency) {
        AppClassLoader classLoader = SoftReferenceUtil.get(loaderMap, dependency);
        if(null == classLoader || !equals(classLoader, dependency)) {
            boolean success = loadJar(dependency);
            if (!success) {
                return null;
            }
            classLoader = SoftReferenceUtil.get(loaderMap, dependency);
        }
        return classLoader;
    }

    /**
     * 装载jar包
     * @param dependency
     * @return
     */
    public boolean loadJar(Dependency dependency) {
        String jarUrl = dependency.getUrl();
        if(null == jarUrl) {
            jarUrl = artifactFetch.getJarUrl(dependency);
        }
        if(null == jarUrl) {
            return false;
        }

        AppClassLoader classLoader = SoftReferenceUtil.get(loaderMap, dependency);
        if(null != classLoader) {
            // 路径不变
            if(classLoader.getUrl().equals(jarUrl)) {
                classLoader.setTimestamp(System.currentTimeMillis());
                return true;
            }
            else {
                classLoader.destroy();
            }
        }

        try {
            classLoader = newClassLoader(jarUrl, getUrls(dependency.getDependencyList()), dependency.getVersion());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }

        classLoader.setTimestamp(System.currentTimeMillis());
        SoftReferenceUtil.put(loaderMap, dependency, classLoader);
        return true;
    }

    private List<String> getUrls(List<Dependency> dependencyList) {
        if(null == dependencyList) {
            return null;
        }

        List<String> dependencies = new ArrayList<>(dependencyList.size());
        for(Dependency dependency : dependencyList) {
            String jarUrl = dependency.getUrl();
            if(null == jarUrl) {
                jarUrl = artifactFetch.getJarUrl(dependency);
            }
            dependencies.add(jarUrl);
        }
        return dependencies;
    }

    /**
     * 获取类加载器
     * @return
     */
    private AppClassLoader newClassLoader(String jarUrl, List<String> dependencies, String version) throws MalformedURLException {
        return new AppClassLoader(jarUrl, dependencies, version, applicationContext.getClassLoader());
    }

    /**
     * 判断请求依赖与现在类加载器信息是否一致
     * @param classLoader
     * @param dependency
     * @return
     */
    private boolean equals(AppClassLoader classLoader, Dependency dependency) {
        if(null != dependency.getUrl()) {
            // 存在指定路径并且相同
            if(classLoader.getUrl().equals(dependency.getUrl())) {
                return true;
            }
            return false;
        }

        long millis = System.currentTimeMillis();
        if(null != dependency.getVersion()) {
            // 存在指定版本并且相同
            if(classLoader.getVersion().equals(dependency.getVersion()) &&
                    millis < classLoader.getTimestamp() + CACHE_TIME) {
                return true;
            }
            return false;
        }

        // 缓存时间
        if(millis < classLoader.getTimestamp() + CACHE_TIME) {
            return true;
        }
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
