package cn.xueyikang.dubbo.faker.core.common;

public class MethodRequest {

    private Integer id;

    private String className;

    private String methodName;

    private String argsTypeName;

    private String returnTypeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getArgsTypeName() {
        return argsTypeName;
    }

    public void setArgsTypeName(String argsTypeName) {
        this.argsTypeName = argsTypeName;
    }

    public String getReturnTypeName() {
        return returnTypeName;
    }

    public void setReturnTypeName(String returnTypeName) {
        this.returnTypeName = returnTypeName;
    }
}
