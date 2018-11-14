package cn.moyada.sharingan.rpc.api.invoke;

import cn.moyada.sharingan.common.exception.InstanceNotFountException;
import cn.moyada.sharingan.common.util.ParamUtil;

/**
 * 异步方法调用器
 * @author xueyikang
 * @since 0.0.1
 */
public abstract class AsyncMethodInvoke<T> extends MethodInvoke<T> implements AsyncInvoke, InvokeProxy {

    private InvokeReceiver invokeReceiver;

    @Override
    public void register(InvokeReceiver invokeReceiver) {
        this.invokeReceiver = invokeReceiver;
    }

    @Override
    public void initialize(InvocationMetaDate metaDate) throws InstanceNotFountException {

    }

    @Override
    public void call(Invocation invocation) {
        Result result = execute(invocation);
        result.setArguments(ParamUtil.toString(invocation.getArgsValue()));
        invokeReceiver.callback(result);
    }
}
