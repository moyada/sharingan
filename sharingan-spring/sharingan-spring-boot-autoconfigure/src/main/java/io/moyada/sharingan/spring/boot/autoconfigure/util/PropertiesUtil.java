package io.moyada.sharingan.spring.boot.autoconfigure.util;

import org.springframework.core.env.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class PropertiesUtil {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static <T> T get(Environment environment, String key, Class<T> clazz) {
        T value = getFromSpring(environment, key, clazz);
        if (null != value) {
            return value;
        }

        return getFromSystem(key, clazz);
    }

    public static <T> T getFromSpring(Environment environment, String key, Class<T> clazz) {
        return environment.getProperty(key, clazz);
    }

    public static <T> T getFromSystem(String key, Class<T> clazz) {
        String value = System.getProperty(key);
        if (null == value) {
            return null;
        }

        try {
            return convert(value, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T convert(String value, Class<T> clazz) throws ParseException {
        if (clazz == String.class) {
            return (T) value;
        } else if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (clazz == boolean.class || clazz == Boolean.class) {
            return value.equalsIgnoreCase("true") ? (T) Boolean.TRUE :
                    value.equalsIgnoreCase("false") ? (T) Boolean.FALSE : null;
        } else if (clazz == short.class || clazz == Short.class) {
            return (T) Short.valueOf(value);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(value);
        } else if (clazz == float.class || clazz == Float.class) {
            return (T) Float.valueOf(value);
        } else if (clazz == double.class || clazz == Double.class) {
            return (T) Double.valueOf(value);
        } else if (clazz == char.class || clazz == Character.class) {
            return (T) (Character) value.charAt(0);
        } else if (clazz == Timestamp.class) {
            return (T) Timestamp.valueOf(value);
        } else if (clazz == Date.class) {
            return (T) Date.valueOf(value);
        } else if (clazz == Time.class) {
            return (T) Time.valueOf(value);
        } else if (clazz == java.util.Date.class) {
            return (T) dateFormat.parse(value);
        } else {
            return null;
        }
    }

    /**
     * 获取属性 map
     * @param environment
     * @param prefix
     * @return
     */
    public static Map<String, String> getMap(Environment environment, String prefix) {
        Map<String, String> map = null;
        int index = prefix.length();

        MutablePropertySources propSrcs = ((AbstractEnvironment) environment).getPropertySources();

        Iterator<PropertySource<?>> iterator = propSrcs.iterator();
        while (iterator.hasNext()) {

            PropertySource<?> propertySource = iterator.next();
            if (propertySource instanceof MapPropertySource) {

                MapPropertySource mapPropertySource = (MapPropertySource) propertySource;
                // 排除系统属性
                if (mapPropertySource.getName().startsWith("system")) {
                    continue;
                }

                Map<String, Object> sourceMap = mapPropertySource.getSource();
                if (sourceMap.isEmpty()) {
                    continue;
                }

                for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
                    String propName = entry.getKey();
                    // 过滤前缀
                    if (!propName.startsWith(prefix)) {
                        continue;
                    }

                    String param = RegexUtil.getParam(propName.substring(index));
                    if (null == param) {
                        continue;
                    }

                    if (null == map) {
                        map = new HashMap<>();
                    }
                    map.put(param, entry.getValue().toString());
                }
            }
        }

        return map;
    }
}
