package io.moyada.sharingan.serialization.api;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class AbstractSerializer {

    protected final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
                return (T) Character.valueOf(json.charAt(0));

            case "boolean":
            case "java.lang.Boolean":
                return json.equalsIgnoreCase("true") ? (T) Boolean.TRUE :
                        json.equalsIgnoreCase("false") ? (T) Boolean.FALSE : null;
            case "byte":
            case "java.lang.Byte":
                return (T) Byte.valueOf(json);
//            case "java.util.Date":
//                try {
//                    return (T) DATE_FORMAT.parse(json);
//                } catch (ParseException e) {
//                    // pass
//                }
//            case "java.sql.Timestamp":
//                return (T) Timestamp.valueOf(json);
//            case "java.sql.Date":
//                return (T) Date.valueOf(json);
//            case "java.sql.Time":
//                return (T) Time.valueOf(json);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    protected <T> T toDataType(String json, Class<T> clazz) {
        if (clazz == java.util.Date.class) {
            try {
                return (T) DATE_FORMAT.parse(json);
            } catch (ParseException e) {
                // pass
            }
        }
        else if (clazz == Timestamp.class) {
            return (T) Timestamp.valueOf(json);
        }
        else if (clazz == Date.class) {
            return (T) Date.valueOf(json);
        }
        else if (clazz == Time.class) {
            return (T) Time.valueOf(json);
        }
        return null;
    }
}
