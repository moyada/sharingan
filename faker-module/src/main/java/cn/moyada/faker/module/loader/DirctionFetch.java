package cn.moyada.faker.module.loader;

import cn.moyada.faker.module.Dependency;

import java.lang.invoke.MethodHandles;

public class DirctionFetch implements ModuleFetch {

    @Override
    public Class getClass(Dependency dependency, String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    @Override
    public MethodHandles.Lookup getMethodLookup(Dependency dependency) {
        return MethodHandles.lookup();
    }
}
