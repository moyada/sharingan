package cn.moyada.sharingan.module.fetch;

import cn.moyada.sharingan.common.enums.PrimitiveClass;
import cn.moyada.sharingan.module.Dependency;

import java.lang.invoke.MethodHandles;

/**
 * 默认元类获取器
 * @author xueyikang
 * @since 1.0
 */
public class DefaultFetch implements MetadataFetch {

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
