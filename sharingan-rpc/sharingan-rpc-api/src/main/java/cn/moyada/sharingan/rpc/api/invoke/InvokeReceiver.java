package cn.moyada.sharingan.rpc.api.invoke;

/**
 * 调用接收者
 * @author xueyikang
 * @since 1.0
 */
public interface InvokeReceiver {

    /**
     * 异步回调
     * @param result
     */
    void callback(Result result);
}
