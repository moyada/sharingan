package cn.moyada.sharingan.rpc.api.invoke;

/**
 * 调用器
 * @author xueyikang
 * @since 0.0.1
 */
public interface Invoke {

    /**
     * 接口调用，返回结果、耗时、异常
     * @param invocation
     * @return
     */
    Result execute(Invocation invocation);
}
