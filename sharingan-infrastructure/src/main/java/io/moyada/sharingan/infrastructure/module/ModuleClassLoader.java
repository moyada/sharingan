package io.moyada.sharingan.infrastructure.module;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 模块类加载器
 * @author xueyikang
 * @since 0.0.1
 */
public abstract class ModuleClassLoader extends URLClassLoader {

    public ModuleClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    /**
     * 查找类，先从当前类加载器中寻找，再通过双亲委托模型获取
     * @param name 类名
     * @return
     * @throws ClassNotFoundException
     */
    abstract Class<?> loadLocalClass(String name) throws ClassNotFoundException;

    /**
     * 卸载类加载器
     */
    public abstract void destroy();
}
