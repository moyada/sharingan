package cn.moyada.sharingan.rpc.api.invoke;

import cn.moyada.sharingan.common.utils.ParamUtil;

public abstract class AsyncMethodInvoke extends MethodInvoke implements AsyncInvoke {

    private InvokeCallback invokeCallback;

    @Override
    public void register(InvokeCallback invokeCallback) {
        this.invokeCallback = invokeCallback;
    }

    @Override
    public void call(Invocation invocation) {
        Result result = super.execute(invocation);
        result.setArguments(ParamUtil.toString(invocation.getArgsValue()));
        invokeCallback.callback(result);
    }
}
