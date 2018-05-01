package cn.moyada.dubbo.faker.core.loader;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

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

        // 尝试查找
        try {
            clazz = findClass(name);
        }
        catch (ClassNotFoundException e) {
            clazz = null;
        }
        if(null != clazz) {
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

    public AppClassLoader(String jarUrl, String version, URLClassLoader parent) throws MalformedURLException {
        super(new URL[]{new URL(jarUrl)}, parent);
        this.url = jarUrl;
        this.version = version;
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
