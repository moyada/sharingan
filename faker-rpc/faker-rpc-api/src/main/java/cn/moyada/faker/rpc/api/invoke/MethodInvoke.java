package cn.moyada.faker.rpc.api.invoke;

import cn.moyada.faker.common.constant.TimeConstant;
import cn.moyada.faker.common.exception.UnsupportedParamNumberException;

import java.lang.invoke.MethodHandle;

public abstract class MethodInvoke implements Invoke {

    protected MethodHandle methodHandle;

    protected Object instance;

    @Override
    public Result execute(Invocation invocation) {
        Object[] argsValue = invocation.getArgsValue();

        long begin = System.nanoTime();

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
                result = Result.success(methodHandle.invoke(invocation));
            }
        } catch (Throwable throwable) {
            result = Result.failed(throwable.getMessage());
        }

        // 完成计算耗时
        result.setResponseTime((System.nanoTime() - begin) / TimeConstant.NANO_PER_MILLIS);
        return result;
    }
}
