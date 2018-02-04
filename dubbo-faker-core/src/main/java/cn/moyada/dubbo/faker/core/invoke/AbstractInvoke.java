package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.exception.UnsupportedParamNumberException;

import java.lang.invoke.MethodHandle;

public abstract class AbstractInvoke {

    public abstract void invoke(MethodHandle handle, Object service, Object[] argsValue, String realParam);

    public abstract void destroy();

    protected Object execute(MethodHandle handle, Object service, Object[] argsValue) throws Throwable {
        if(null == argsValue) {
            return handle.invoke(service);
        }
        switch (argsValue.length) {
            case 0:
                return handle.invoke(service);
            case 1:
                return handle.invoke(service, argsValue[0]);
            case 2:
                return handle.invoke(service, argsValue[0], argsValue[1]);
            case 3:
                return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2]);
            case 4:
                return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3]);
            case 5:
                return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4]);
            case 6:
                return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4], argsValue[5]);
            case 7:
                return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4], argsValue[5], argsValue[6]);
            default:
                return new UnsupportedParamNumberException("[Faker Invoker Error] Param number not yet support.");
        }
    }
}
