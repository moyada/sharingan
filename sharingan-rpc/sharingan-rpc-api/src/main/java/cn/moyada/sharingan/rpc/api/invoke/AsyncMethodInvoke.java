package cn.moyada.sharingan.rpc.api.invoke;

import cn.moyada.sharingan.common.utils.ParamUtil;

/**
 * 异步方法
 * @author xueyikang
 * @since 1.0
 */
public abstract class AsyncMethodInvoke extends MethodInvoke implements AsyncInvoke {

    private InvokeReceiver invokeReceiver;

    @Override
    public void register(InvokeReceiver invokeReceiver) {
        this.invokeReceiver = invokeReceiver;
    }

    @Override
    public void call(Invocation invocation) {
        Result result = super.execute(invocation);
        result.setArguments(ParamUtil.toString(invocation.getArgsValue()));
        invokeReceiver.callback(result);
    }
}
