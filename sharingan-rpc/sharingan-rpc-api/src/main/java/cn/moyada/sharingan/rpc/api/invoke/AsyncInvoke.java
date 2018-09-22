package cn.moyada.sharingan.rpc.api.invoke;

/**
 * 异步调用者
 */
public interface AsyncInvoke {

    /**
     * 注册调用接受者
     * @param invokeReceiver
     */
    void register(InvokeReceiver invokeReceiver);

    /**
     * 请求
     * @param invocation
     */
    void call(Invocation invocation);
}
