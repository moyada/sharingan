package cn.moyada.dubbo.faker.api.common;

import java.io.Serializable;

public class FakerRecord implements Serializable {

    private static final long serialVersionUID = 1364639062864872337L;

    private String applicationName;

    private String className;

    private String methodName;

    private String parameterTypes;

    private String returnType;

    private String methodParameters;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
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

    public String getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(String parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getMethodParameters() {
        return methodParameters;
    }

    public void setMethodParameters(String methodParameters) {
        this.methodParameters = methodParameters;
    }

    @Override
    public String toString() {
        return "FakerRecord{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes='" + parameterTypes + '\'' +
                ", returnType='" + returnType + '\'' +
                ", methodParameters='" + methodParameters + '\'' +
                '}';
    }
}
