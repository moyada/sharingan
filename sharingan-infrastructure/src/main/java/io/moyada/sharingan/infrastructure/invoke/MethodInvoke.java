package io.moyada.sharingan.infrastructure.invoke;


import io.moyada.sharingan.infrastructure.exception.InstanceNotFountException;
import io.moyada.sharingan.infrastructure.invoke.data.InvocationMetaDate;
import io.moyada.sharingan.infrastructure.invoke.data.Result;
import io.moyada.sharingan.infrastructure.util.ClassUtil;
import io.moyada.sharingan.infrastructure.util.TimeUtil;

import java.sql.Timestamp;

/**
 * 方法调用器
 * @author xueyikang
 * @since 0.0.1
 */
public abstract class MethodInvoke<T, I extends InvocationMetaDate> implements Invoke {

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(InvocationMetaDate metaDate) throws InstanceNotFountException {
        Class<?> metaClass = ClassUtil.getGenericType(this, MethodInvoke.class, "I");
        if (metaClass.isInstance(metaDate)) {
            doInitialize((I) metaDate);
            beforeInvoke();
        } else {
            throw new IllegalArgumentException("Generic type do not match");
        }
    }

    protected void doInitialize(I metaDate) throws InstanceNotFountException {
    }

    @Override
    public void destroy() {
    }

    protected void beforeInvoke() {
    }

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

        // 完成计算耗时
        result.setResponseTime((int) (TimeUtil.currentTimeMillis() - begin));
        result.setStartTime(new Timestamp(begin));
        return result;
    }
}
