package cn.moyada.sharingan.module.fetch;

import cn.moyada.sharingan.module.Dependency;
import cn.moyada.sharingan.module.loader.AppClassLoader;
import cn.moyada.sharingan.module.loader.ClassLoaderAction;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
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
@Component("moduleFetch")
public class ModuleFetch extends DirectionFetch implements ApplicationContextAware, MetadataFetch {

    private static final int CACHE_TIME = 720000;

    private ClassLoader parent;

    private final Map<Dependency, AppClassLoader> loaderMap = new HashMap<>();

    @Autowired
    private DependencyFetch dependencyFetch;

    @Override
    public void checkoutClassLoader(Dependency dependency) {
        AppClassLoader classLoader = getClassLoader(dependency);
        if(null == classLoader) {
            return;
        }
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    @Override
    public void recover() {
        Thread.currentThread().setContextClassLoader(parent);
    }

    @Override
    public Class getClass(Dependency dependency, String className) throws ClassNotFoundException {
        Class<?> clazz = PrimitiveClass.forName(className);
        if (null != clazz) {
            return clazz;
        }

        ClassLoaderAction classLoader = getClassLoader(dependency);
        if(null == classLoader) {
            throw new ClassNotFoundException(dependency + ", " + className);
        }
        clazz = classLoader.loadLocalClass(className);
        return clazz;
    }

    @Override
    public MethodHandles.Lookup getMethodLookup(Dependency dependency) {
        ClassLoaderAction classLoader = getClassLoader(dependency);
        if(null == classLoader) {
            return MethodHandles.lookup();
        }
        return classLoader.getMethodLookup();
    }

    /**
     * 获取对应依赖类加载器
     * @param dependency
     * @return
     */
    private AppClassLoader getClassLoader(Dependency dependency) {
        AppClassLoader classLoader = loaderMap.get(dependency);
        if(null == classLoader || !equals(classLoader, dependency)) {
            boolean success = loadJar(dependency);
            if (!success) {
                return null;
            }
            classLoader = loaderMap.get(dependency);
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
            jarUrl = dependencyFetch.getJarUrl(dependency);
        }
        if(null == jarUrl) {
            return false;
        }

        AppClassLoader classLoader = loaderMap.get(dependency);
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
        loaderMap.put(dependency, classLoader);
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
                jarUrl = dependencyFetch.getJarUrl(dependency);
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
        return new AppClassLoader(jarUrl, dependencies, version, parent);
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

    public static void main(String[] args) throws ClassNotFoundException {
        System.setProperty("maven.host", "https://repo.souche-inc.com");
        System.setProperty("maven.version", "maven2");
        ModuleFetch moduleFetch = new ModuleFetch();
        Dependency dependency = new Dependency();
        dependency.setGroupId("com.souche");
        dependency.setArtifactId("car-model-api");
        Class aClass = moduleFetch.getClass(dependency, "com.souche.car.model.api.model.ModelService");
        System.out.println(aClass);
        moduleFetch.getClassLoader(dependency).destroy();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.parent = applicationContext.getClassLoader();
    }
}
