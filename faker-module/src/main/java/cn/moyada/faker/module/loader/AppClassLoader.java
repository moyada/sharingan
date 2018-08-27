package cn.moyada.faker.module.loader;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 项目加载器
 * @author xueyikang
 * @create 2018-04-28 08:17
 */
public class AppClassLoader extends URLClassLoader implements ClassLoaderAction {

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
    public Class<?> loadLocalClass(String name) throws ClassNotFoundException {
        // 是否已加载过
        Class<?> clazz = findLoadedClass(name);
        if(null != clazz) {
            return clazz;
        }

        synchronized (this) {
            // 尝试查找
            try {
                clazz = this.findClass(name);
            } catch (Exception e) {
                clazz = null;
            }
        }
        if(null != clazz) {
            resolveClass(clazz);
            return clazz;
        }

        // 交给父加载器
        return super.loadClass(name);
    }

    @Override
    public MethodHandles.Lookup getMethodLookup() {
        return MethodHandles.lookup();
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
