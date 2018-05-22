package cn.moyada.dubbo.faker.core.loader;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * 项目加载器
 * @author xueyikang
 * @create 2018-04-28 08:17
 */
public class AppClassLoader extends URLClassLoader {

    /**
     * 版本
     */
    private String version;

    /**
     * jar包链接
     */
    private String url;

    private long timestamp;

    /**
     * 查找类，先从当前类加载器中寻找，再通过双亲委托模型获取
     * @param name 类名
     * @return
     * @throws ClassNotFoundException
     */
    protected Class<?> loadLocalClass(String name) throws ClassNotFoundException {
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

    /**
     * 获取当前类加载器的方法具柄Lookup
     * @return
     */
    public MethodHandles.Lookup getMethodLookup() {
        return MethodHandles.lookup();
    }

    public AppClassLoader(String jarUrl, List<String> dependencies, String version, URLClassLoader parent) throws MalformedURLException {
        super(buildURL(jarUrl, dependencies), parent);
        this.url = jarUrl;
        this.version = version;
    }

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
