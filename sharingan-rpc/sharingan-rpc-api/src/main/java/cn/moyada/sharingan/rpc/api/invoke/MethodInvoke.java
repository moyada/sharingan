package cn.moyada.sharingan.rpc.api.invoke;

import cn.moyada.sharingan.common.util.TimeUtil;

import java.lang.invoke.MethodHandle;
import java.sql.Timestamp;

/**
 * 方法调用器
 * @author xueyikang
 * @since 0.0.1
 */
public abstract class MethodInvoke<T> implements Invoke {

    // 方法句柄
    protected MethodHandle methodHandle;

    // 目标实例
    protected Object instance;

    /**
     * 解析调用参数
     * @param invocation
     * @return
     */
    protected abstract T resolve(Invocation invocation);

    /**
     * 接口请求
     * @param invocation
     * @return
     */
    protected abstract Result invoke(T invocation);

    @Override
    public Result execute(Invocation invocation) {
        T args = resolve(invocation);

        long begin = TimeUtil.currentTimeMillis();

        Result result = invoke(args);

        result.setStartTime(new Timestamp(begin));
        // 完成计算耗时
        result.setResponseTime((int) (TimeUtil.currentTimeMillis() - begin));
        return result;
    }
}
