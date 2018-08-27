package cn.moyada.faker.module.fetch;

import cn.moyada.faker.module.Dependency;

import java.lang.invoke.MethodHandles;

public interface MetadataFetch {

    void checkoutClassLoader(Dependency dependency);

    /**
     * 根据依赖获取类
     * @param dependency 依赖
     * @param className 类全名
     * @return
     * @throws ClassNotFoundException
     */
    Class getClass(Dependency dependency, String className) throws ClassNotFoundException;

    /**
     * 获取对应依赖方法具柄
     * @param dependency
     * @return
     */
    MethodHandles.Lookup getMethodLookup(Dependency dependency);
}
