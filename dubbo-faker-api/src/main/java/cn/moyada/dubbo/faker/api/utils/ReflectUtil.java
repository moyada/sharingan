package cn.moyada.dubbo.faker.api.utils;

/**
 * @author xueyikang
 * @create 2018-03-30 01:05
 */
public class ReflectUtil {

    public static String getParameterTypeName(Class<?>[] parameterTypes) {
        if(null == parameterTypes || parameterTypes.length == 0) {
            return ",";
        }

        StringBuilder paramType = new StringBuilder();
        for (Class<?> parameterType : parameterTypes) {
            paramType.append(parameterType.getName());
            paramType.append(",");
        }
        return paramType.substring(0, paramType.length() - 1);
    }

    public static String getReturnTypeName(Class<?> returnType) {
        if(null == returnType) {
            return "java.lang.Void";
        }
        return returnType.getName();
    }
}
