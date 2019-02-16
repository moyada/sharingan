package io.moyada.sharingan.domain.metadada;


import io.moyada.sharingan.infrastructure.invoke.data.ClassInvocation;
import io.moyada.sharingan.infrastructure.invoke.data.HttpInvocation;
import io.moyada.sharingan.infrastructure.util.StringUtil;


/**
 * @author xueyikang
 * @since 1.0
 **/
public class InvokeData {

    private MethodData methodData;

    private HttpData httpData;

    public InvokeData(MethodData methodData) {
        this.methodData = methodData;
    }

    public InvokeData(HttpData httpData) {
        this.httpData = httpData;
    }

    public ClassInvocation getClassInvocation(ClassData classData) {
        ServiceData serviceData = methodData.getServiceData();
        AppData appData = serviceData.getAppData();

        return new ClassInvocation(appData.getName(), serviceData.getName(), methodData.getMethodName(),
                classData.getClassType(), classData.getMethodHandle());
    }

    public HttpInvocation getHttpInvocation() {
        if (null == httpData) {
            return null;
        }
        ServiceData serviceData = httpData.getServiceData();
        AppData appData = serviceData.getAppData();

        String param = httpData.getParam().replaceAll(" ", "");
        String[] params = StringUtil.isEmpty(param) ? null : param.split(",");

        String header = httpData.getHeader().replaceAll(" ", "");
        String[] headers = StringUtil.isEmpty(header) ? null : header.split(",");

        return new HttpInvocation(appData.getName(), serviceData.getName(), httpData.getMethodName(),
                httpData.getMethodType(), headers, params);
    }

    public ServiceData getServiceData() {
        ServiceData serviceData;
        if (null == httpData) {
            serviceData = methodData.getServiceData();
        } else {
            serviceData = httpData.getServiceData();
        }
        return serviceData;
    }

    public MethodData getMethodData() {
        return methodData;
    }

    public HttpData getHttpData() {
        return httpData;
    }
}
