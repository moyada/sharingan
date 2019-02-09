package io.moyada.sharingan.domain.task;


import io.moyada.sharingan.infrastructure.invoke.data.Result;

/**
 * 结果处理器
 * @author xueyikang
 * @create 2018-08-27 14:54
 */
public interface RecordHandler<T> {

    /**
     * 接收数据处理
     * @param result
     * @return
     */
    T receive(Result result);
}
