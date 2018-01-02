package cn.moyada.dubbo.faker.core.common;

import java.lang.invoke.MethodHandle;
import java.util.List;

public class HandleInfo {

    private MethodHandle handle;
    private Object service;
    private String className;
    private String methodName;
    private List<Object> argsValue;

    public HandleInfo() {
    }

    public HandleInfo(MethodHandle handle, Object service, String className, String methodName, List<Object> argsValue) {
        this.handle = handle;
        this.service = service;
        this.className = className;
        this.methodName = methodName;
        this.argsValue = argsValue;
    }

    public MethodHandle getHandle() {
        return handle;
    }

    public void setHandle(MethodHandle handle) {
        this.handle = handle;
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<Object> getArgsValue() {
        return argsValue;
    }

    public void setArgsValue(List<Object> argsValue) {
        this.argsValue = argsValue;
    }
}
