package io.moyada.sharingan.infrastructure.invoke;

import io.moyada.sharingan.infrastructure.exception.InstanceNotFountException;
import io.moyada.sharingan.infrastructure.invoke.data.InvocationMetaDate;
import io.moyada.sharingan.infrastructure.invoke.data.Result;

/**
 * 调用器
 * @author xueyikang
 * @since 0.0.1
 */
public interface Invoke {

    /**
     * 初始化环境
     * @param metaDate
     * @throws IllegalArgumentException
     * @throws InstanceNotFountException
     */
    void initialize(InvocationMetaDate metaDate) throws InstanceNotFountException;

    /**
     * 接口调用，返回结果、耗时、异常
     * @param invocation
     * @return
     */
    Result execute(Invocation invocation);
}
