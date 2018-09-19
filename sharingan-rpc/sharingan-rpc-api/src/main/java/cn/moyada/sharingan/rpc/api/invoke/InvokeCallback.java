package cn.moyada.sharingan.rpc.api.invoke;

public interface InvokeCallback {

    /**
     * 异步回调
     * @param result
     */
    void callback(Result result);
}
