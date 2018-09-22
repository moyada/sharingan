package cn.moyada.sharingan.rpc.api.invoke;

/**
 * 请求数据
 * @author xueyikang
 * @since 1.0
 */
public class Invocation {

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 调用参数
     */
    private Object[] argsValue;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgsValue() {
        return argsValue;
    }

    public void setArgsValue(Object[] argsValue) {
        this.argsValue = argsValue;
    }
}
