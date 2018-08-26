package cn.moyada.dubbo.faker.core.loader;

import cn.moyada.dubbo.faker.core.common.BeanHolder;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 动态状态依赖
 * @author xueyikang
 * @create 2018-04-27 16:33
 */
@Component
public class ModuleLoader {

    private static final int CACHE_TIME = 720000;

//    @Autowired
//    private FakerManager fakerManager;

    @Autowired
    private FetchLastJar fetchLastJar;

    private final Map<Dependency, AppClassLoader> loaderMap;

    public ModuleLoader() {
        this.loaderMap = new HashMap<>();
    }

    /**
     * 根据依赖获取类
     * @param dependency 依赖
     * @param className 类全名
     * @return
     * @throws ClassNotFoundException
     */
    public Class getClass(Dependency dependency, String className) throws ClassNotFoundException {
        AppClassLoader classLoader = getClassLoader(dependency);
        if(null == classLoader) {
            throw new ClassNotFoundException(dependency + ", " + className);
        }
        return classLoader.loadLocalClass(className);
    }

    /**
     * 获取对应依赖方法具柄
     * @param dependency
     * @return
     */
    public MethodHandles.Lookup getMethodLookup(Dependency dependency) {
        AppClassLoader classLoader = getClassLoader(dependency);
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
    public AppClassLoader getClassLoader(Dependency dependency) {
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
            jarUrl = fetchLastJar.getJarUrl(dependency);
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

        // 更新链接
//        if(!jarUrl.equals(dependency.getUrl())) {
//            fakerManager.updateUrl(dependency.getGroupId(), dependency.getArtifactId(), jarUrl);
//        }
        classLoader.setTimestamp(System.currentTimeMillis());
        loaderMap.put(dependency, classLoader);
        return true;
    }

    private List<String> getUrls(List<Dependency> dependencyList) {
        if(null == dependencyList) {
            return null;
        }

        List<String> dependencies = Lists.newArrayListWithExpectedSize(dependencyList.size());
        for(Dependency dependency : dependencyList) {
            String jarUrl = dependency.getUrl();
            if(null == jarUrl) {
                jarUrl = fetchLastJar.getJarUrl(dependency);
            }
            dependencies.add(jarUrl);
        }
        return dependencies;
    }

    /**
     * 清空类加载器
     * @param classLoader URL类加载器
     */
    private void clear(URLClassLoader classLoader) {
        ResourceBundle.clearCache(classLoader);
        try {
            classLoader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取类加载器
     * @return
     */
    private AppClassLoader newClassLoader(String jarUrl, List<String> dependencies, String version) throws MalformedURLException {
        return new AppClassLoader(jarUrl, dependencies, version, BeanHolder.getSpringClassLoader());
//        return new AppClassLoader(jarUrl, version, (URLClassLoader) ClassLoader.getSystemClassLoader());
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

    class ClearExpireLoaderThread implements Runnable {

        @Override
        public void run() {
            long time;
            for (;;) {
                time = System.currentTimeMillis();

                for (Map.Entry<Dependency, AppClassLoader> entry : loaderMap.entrySet()) {
                    if(entry.getValue().getTimestamp() + CACHE_TIME < time) {
                        clear(entry.getValue());
                        loaderMap.remove(entry.getKey());
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        System.setProperty("maven.host", "https://repo.souche-inc.com");
        System.setProperty("maven.version", "maven2");
        ModuleLoader moduleLoader = new ModuleLoader();
        Dependency dependency = new Dependency();
        dependency.setGroupId("com.souche");
        dependency.setArtifactId("car-model-api");
        Class aClass = moduleLoader.getClass(dependency, "com.souche.car.model.api.model.ModelService");
        System.out.println(aClass);
        moduleLoader.getClassLoader(dependency).destroy();
    }
}
