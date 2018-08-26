package cn.moyada.faker.rpc.api.invoke;

public interface AsyncInvoke extends RegisterCallback {

    void call(Invocation invocation);
}
