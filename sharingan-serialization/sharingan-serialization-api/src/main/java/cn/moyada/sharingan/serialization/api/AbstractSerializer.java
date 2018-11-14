package cn.moyada.sharingan.serialization.api;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class AbstractSerializer {

    protected static boolean isEmpty(String json) {
        return null == json || Constant.NULL.equals(json);
    }

    /**
     * java 原生类型
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    protected  <T> T toPrimitiveType(String json, Class<T> clazz) {

        if(clazz == String.class) {
            return (T) json;
        }

        if(clazz.isEnum()) {
            Enum anEnum = Enum.valueOf((Class<Enum>) clazz, json);
            return (T) anEnum;
        }

        switch (clazz.getName()) {
            case "int":
            case "java.lang.Integer":
                return (T) Integer.valueOf(json);

            case "long":
            case "java.lang.Long":
                return (T) Long.valueOf(json);

            case "short":
            case "java.lang.Short":
                return (T) Short.valueOf(json);

            case "double":
            case "java.lang.Double":
                return (T) Double.valueOf(json);

            case "float":
            case "java.lang.Float":
                return (T) Float.valueOf(json);

            case "char":
            case "java.lang.Character":
                return (T) (Character) json.charAt(0);

            case "boolean":
            case "java.lang.Boolean":
                return (T) Boolean.valueOf(json);

            case "byte":
            case "java.lang.Byte":
                return (T) Byte.valueOf(json);
        }

        return null;
    }
}
