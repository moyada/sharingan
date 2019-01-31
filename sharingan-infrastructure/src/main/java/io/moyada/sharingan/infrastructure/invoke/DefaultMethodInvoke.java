package io.moyada.sharingan.infrastructure.invoke;


import io.moyada.sharingan.infrastructure.exception.UnsupportedParamNumberException;
import io.moyada.sharingan.infrastructure.invoke.data.InvocationMetaDate;
import io.moyada.sharingan.infrastructure.invoke.data.Result;

import java.lang.invoke.MethodHandle;

/**
 * 方法具柄调用器
 * @author xueyikang
 * @since 0.0.1
 **/
public class DefaultMethodInvoke<I extends InvocationMetaDate> extends AsyncMethodInvoke<Object[], I> {

    // 方法句柄
    protected MethodHandle methodHandle;

    // 目标实例
    protected Object instance;

    @Override
    protected Object[] resolve(Invocation invocation) {
        return invocation.getArgsValue();
    }

    public void setMethodHandle(MethodHandle methodHandle) {
        this.methodHandle = methodHandle;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    protected Result invoke(Object[] argsValue) {
        Result result;
        try {
            if(null != argsValue) {
                switch (argsValue.length) {
                    case 0:
                        result = Result.success(methodHandle.invoke(instance));
                        break;
                    case 1:
                        result = Result.success(methodHandle.invoke(instance, argsValue[0]));
                        break;
                    case 2:
                        result = Result.success(methodHandle.invoke(instance, argsValue[0], argsValue[1]));
                        break;
                    case 3:
                        result = Result.success(methodHandle.invoke(instance, argsValue[0], argsValue[1], argsValue[2]));
                        break;
                    case 4:
                        result = Result.success(methodHandle.invoke(instance, argsValue[0], argsValue[1], argsValue[2], argsValue[3]));
                        break;
                    case 5:
                        result = Result.success(methodHandle.invoke(instance, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4]));
                        break;
                    case 6:
                        result = Result.success(methodHandle.invoke(instance, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4], argsValue[5]));
                        break;
                    case 7:
                        result = Result.success(methodHandle.invoke(instance, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4], argsValue[5], argsValue[6]));
                        break;
                    default:
                        throw new UnsupportedParamNumberException("[Faker Invoker Error] too many invoke param.");
                }
            }
            else {
                result = Result.success(methodHandle.invoke(instance));
            }
        } catch (Throwable throwable) {
            result = Result.failed(throwable.getMessage());
        }
        return result;
    }
}
