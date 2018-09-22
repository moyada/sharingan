package cn.moyada.sharingan.module.fetch;

import cn.moyada.sharingan.module.Dependency;

import java.lang.invoke.MethodHandles;

/**
 * 元类信息获取器
 * @author xueyikang
 * @since 1.0
 */
public interface MetadataFetch {

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
