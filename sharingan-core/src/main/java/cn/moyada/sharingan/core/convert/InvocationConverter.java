package cn.moyada.sharingan.core.convert;

import cn.moyada.sharingan.config.HttpRequestInfo;
import cn.moyada.sharingan.config.InvokeContext;
import cn.moyada.sharingan.rpc.api.invoke.InvocationMetaDate;

public class InvocationConverter {

    @SuppressWarnings("ConstantConditions")
    public static InvocationMetaDate toInvocation(InvokeContext invokeContext) {
        InvocationMetaDate invocationMetaDate = new InvocationMetaDate();
        invocationMetaDate.setApplicationName(invokeContext.getAppName());
        invocationMetaDate.setServiceName(invokeContext.getServiceName());
        invocationMetaDate.setMethodName(invokeContext.getMethodName());

        HttpRequestInfo httpRequestInfo = invokeContext.getHttpRequestInfo();
        if (null == httpRequestInfo) {
            invocationMetaDate.setHttpType(httpRequestInfo.getHttpType());
            invocationMetaDate.setParam(httpRequestInfo.getParam());
            invocationMetaDate.setHeader(httpRequestInfo.getHeader());
        } else {
            invocationMetaDate.setServiceClass(invokeContext.getInvokeMetaData().getClassType());
            invocationMetaDate.setMethodHandle(invokeContext.getInvokeMetaData().getMethodHandle());
        }

        return invocationMetaDate;
    }
}
