package cn.moyada.faker.rpc.api.invoke;

import java.lang.invoke.MethodHandle;

public class InvocationMetaDate {

    private ClassLoader classLoader;

    private Class service;

    private MethodHandle methodHandle;

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Class getService() {
        return service;
    }

    public void setService(Class service) {
        this.service = service;
    }

    public MethodHandle getMethodHandle() {
        return methodHandle;
    }

    public void setMethodHandle(MethodHandle methodHandle) {
        this.methodHandle = methodHandle;
    }
}
