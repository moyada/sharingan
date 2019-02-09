package io.moyada.sharingan.infrastructure.module;


import io.moyada.sharingan.infrastructure.enums.PrimitiveClass;

import java.lang.invoke.MethodHandles;

/**
 * 默认元类获取器
 * @author xueyikang
 * @since 0.0.1
 */
public class DefaultFetch implements MetadataFetch {

    @Override
    public ClassLoader getClassLoader(Dependency dependency) {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public Class getClass(Dependency dependency, String className) throws ClassNotFoundException {
        Class<?> clazz = PrimitiveClass.forName(className);
        if (null != clazz) {
            return clazz;
        }

        return Class.forName(className);
    }

    @Override
    public MethodHandles.Lookup getMethodLookup(Dependency dependency) {
        return MethodHandles.lookup();
    }
}
