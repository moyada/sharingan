package cn.moyada.faker.module.fetch;

import cn.moyada.faker.module.Dependency;

import java.lang.invoke.MethodHandles;

public abstract class DirectionFetch implements MetadataFetch {

    @Override
    public Class getClass(Dependency dependency, String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    @Override
    public MethodHandles.Lookup getMethodLookup(Dependency dependency) {
        return MethodHandles.lookup();
    }
}
