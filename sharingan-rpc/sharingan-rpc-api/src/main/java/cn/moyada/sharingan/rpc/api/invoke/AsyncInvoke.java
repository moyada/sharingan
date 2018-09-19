package cn.moyada.sharingan.rpc.api.invoke;

public interface AsyncInvoke extends RegisterCallback {

    void call(Invocation invocation);
}
