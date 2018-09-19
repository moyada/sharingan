package cn.moyada.sharingan.core.handler;

import cn.moyada.sharingan.rpc.api.invoke.Result;

/**
 * 处理器
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

    /**
     * 测试编号
     * @return
     */
    String getFakerId();
}
