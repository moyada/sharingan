package cn.moyada.sharingan.rpc.api.invoke;

import cn.moyada.sharingan.common.exception.UnsupportedParamNumberException;
import cn.moyada.sharingan.common.utils.TimeUtil;

import java.lang.invoke.MethodHandle;
import java.sql.Timestamp;

public abstract class MethodInvoke implements Invoke {

    protected MethodHandle methodHandle;

    protected Object instance;

    @Override
    public Result execute(Invocation invocation) {
        Object[] argsValue = invocation.getArgsValue();

        long begin = TimeUtil.currentTimeMillis();

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

        result.setStartTime(new Timestamp(begin));
        // 完成计算耗时
        result.setResponseTime((int) (TimeUtil.currentTimeMillis() - begin));
        return result;
    }
}
