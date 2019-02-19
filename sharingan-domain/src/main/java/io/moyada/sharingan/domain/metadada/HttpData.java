package io.moyada.sharingan.domain.metadada;

import io.moyada.sharingan.infrastructure.invoke.data.HttpInvocation;
import io.moyada.sharingan.infrastructure.util.StringUtil;

/**
 * http请求信息
 * @author xueyikang
 * @since 0.0.1
 **/
public class HttpData extends InvokeData<HttpInvocation> {

    /**
     * 方法编号
     */
    private Integer id;

    /**
     * 请求类别, GET\POST\PUT\DELETE
     */
    private String methodType;

    /**
     * 参数
     */
    private String param;

    /**
     * 头信息
     */
    private String header;

    /**
     * 表达式
     */
    private String expression;

    private HttpData() {
    }

    public HttpData(Integer id, String methodName, String methodType, String param, String header, String expression) {
        super(methodName);
        this.id = id;
        this.methodType = methodType;
        this.param = param;
        this.header = header;
        this.expression = expression;
    }

    @Override
    public HttpInvocation getInvocation() {
        ServiceData serviceData = getServiceData();
        AppData appData = serviceData.getAppData();

        String param = null == getParam() ? null : getParam().replaceAll(" ", "");
        String[] params = StringUtil.isEmpty(param) ? null : param.split(",");

        String header = null == getHeader() ? null : getHeader().replaceAll(" ", "");
        String[] headers = StringUtil.isEmpty(header) ? null : header.split(",");

        return new HttpInvocation(appData.getName(), serviceData.getName(), getMethodName(), getMethodType(), headers, params);
    }

    private void setId(Integer id) {
        this.id = id;
    }

    private void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    private void setParam(String param) {
        this.param = param;
    }

    private void setHeader(String header) {
        this.header = header;
    }

    private void setExpression(String expression) {
        this.expression = expression;
    }

    public Integer getId() {
        return id;
    }

    public String getMethodType() {
        return methodType;
    }

    public String getParam() {
        return param;
    }

    public String getHeader() {
        return header;
    }

    public String getExpression() {
        return expression;
    }
}
