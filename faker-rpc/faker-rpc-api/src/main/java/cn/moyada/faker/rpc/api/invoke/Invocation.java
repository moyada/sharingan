package cn.moyada.faker.rpc.api.invoke;

public class Invocation {

    private String serviceName;

    private String methodName;

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
