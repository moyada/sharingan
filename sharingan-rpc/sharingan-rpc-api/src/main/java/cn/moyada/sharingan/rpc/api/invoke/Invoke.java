package cn.moyada.sharingan.rpc.api.invoke;

/**
 * 调用者
 */
public interface Invoke {

    /**
     * 请求
     * @param invocation
     * @return
     */
    Result execute(Invocation invocation);
}
