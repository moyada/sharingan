package cn.moyada.faker.rpc.api.invoke;

import cn.moyada.faker.common.utils.ParamUtil;

public abstract class AsyncMethodInvoke extends MethodInvoke implements AsyncInvoke {

    private InvokeCallback invokeCallback;

    @Override
    public void register(InvokeCallback invokeCallback) {
        this.invokeCallback = invokeCallback;
    }

    @Override
    public void call(Invocation invocation) {
        Result result = super.execute(invocation);
        result.setArgs(ParamUtil.toString(invocation.getArgsValue()));
        invokeCallback.callback(result);
    }
}
