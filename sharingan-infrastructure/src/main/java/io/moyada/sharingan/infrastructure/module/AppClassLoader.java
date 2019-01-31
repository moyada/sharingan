package io.moyada.sharingan.infrastructure.module;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 项目加载器
 * @author xueyikang
 * @create 2018-04-28 08:17
 */
public class AppClassLoader extends ModuleClassLoader {

    /**
     * 版本
     */
    private String version;

    /**
     * jar包链接
     */
    private String url;

    private long timestamp;

    public AppClassLoader(String jarUrl, List<String> dependencies, String version, ClassLoader parent) throws MalformedURLException {
        super(buildURL(jarUrl, dependencies), parent);
        this.url = jarUrl;
        this.version = version;
    }

    /**
     * 封装依赖包路径
     * @param jarUrl
     * @param dependencies
     * @return
     * @throws MalformedURLException
     */
    protected static URL[] buildURL(String jarUrl, List<String> dependencies) throws MalformedURLException {
        if(null == dependencies) {
            return new URL[]{new URL(jarUrl)};
        }
        URL[] urls = new URL[dependencies.size() + 1];
        urls[0] = new URL(jarUrl);

        int index = 1;
        for (String dependency : dependencies) {
            urls[index++] = new URL(dependency);
        }
        return urls;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz;
        synchronized (getClassLoadingLock(name)) {
            // 是否已加载过
            clazz = findLoadedClass(name);
            if(null != clazz) {
                return clazz;
            }

            // 尝试查找
            try {
                clazz = findClass(name);
            } catch (ClassNotFoundException e) {
                clazz = getParent().loadClass(name);
            }

            resolveClass(clazz);
        }
        return clazz;
    }

    @Override
    public Class<?> loadLocalClass(String name) throws ClassNotFoundException {
        return loadClass(name, true);
    }

    @Override
    public void destroy() {
        ResourceBundle.clearCache(this);
        try {
            this.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
