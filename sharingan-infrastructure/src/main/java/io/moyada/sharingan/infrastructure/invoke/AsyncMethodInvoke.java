package io.moyada.sharingan.infrastructure.invoke;


import io.moyada.sharingan.infrastructure.invoke.data.InvocationMetaDate;
import io.moyada.sharingan.infrastructure.invoke.data.Result;
import io.moyada.sharingan.infrastructure.util.StringUtil;

/**
 * 异步方法调用器
 * @author xueyikang
 * @since 0.0.1
 */
public abstract class AsyncMethodInvoke<T, I extends InvocationMetaDate> extends MethodInvoke<T, I> implements AsyncInvoke, InvokeProxy {

    private InvokeReceiver invokeReceiver;

    @Override
    public void register(InvokeReceiver invokeReceiver) {
        this.invokeReceiver = invokeReceiver;
    }

    @Override
    public void call(Invocation invocation) {
        Result result = execute(invocation);
        result.setArguments(StringUtil.toString(invocation.getArgsValue()));
        invokeReceiver.callback(result);
    }
}
