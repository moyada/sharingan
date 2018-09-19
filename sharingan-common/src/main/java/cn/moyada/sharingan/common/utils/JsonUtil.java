package cn.moyada.sharingan.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xueyikang on 2017/8/6.
 */
public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static final String NULL = "null";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TypeFactory typeFactory = mapper.getTypeFactory();
    private static final Map<String, CollectionType> collectionTypeMap = new ConcurrentHashMap<>();
    private static final Map<String, MapType> mapTypeMap = new ConcurrentHashMap<>();

    static {
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS);

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        //设置JSON时间格式
        mapper.setDateFormat(DATE_FORMAT);
    }

    public static String toJson(Object obj) {
        String json;
        try {
            if(obj instanceof String ||
                    obj instanceof Boolean ||
                    obj instanceof Number ||
                    obj instanceof Character) {
                json = obj.toString();
            }
            else {
                json = mapper.writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            log.error("Serialization json Error: " + e.getMessage());
            return null;
        }
        return json;
    }

    @SuppressWarnings("unchecked")
    public static <C> C toObject(String json, Class<C> c) {
        if(null == json || NULL.equals(json)) {
            return null;
        }
        if(c == Object.class || c == String.class) {
            return (C) json;
        }

        if(c.isEnum()) {
            Enum anEnum = Enum.valueOf((Class<Enum>) c, json);
            return (C) anEnum;
        }

        switch (c.getName()) {
//            case "java.lang.String":
//                return (C) json;

            case "java.lang.Integer":
                return (C) Integer.valueOf(json);

            case "java.lang.Long":
                return (C) Long.valueOf(json);

            case "java.lang.Short":
                return (C) Short.valueOf(json);

            case "java.lang.Double":
                return (C) Double.valueOf(json);

            case "java.lang.Float":
                return (C) Float.valueOf(json);

            case "java.lang.Character":
                return (C) (Character) json.charAt(0);

            case "java.lang.Boolean":
                return (C) Boolean.valueOf(json);

            case "java.lang.Byte":
                return (C) Byte.valueOf(json);

//            case "java.util.Date":
//                try {
//                    return (C) DATE_FORMAT.parse(json);
//                } catch (ParseException e) {
//                    log.error("Failed to parse java.util.Date " + json + " by data format, cause: " + e.getMessage());
//                    return null;
//                }
//            case "java.sql.Time":
//                try {
//                    return (C) Time.valueOf(json);
//                } catch (Exception e) {
//                    log.error("Failed to parse java.sql.Time " + json + " by data format, cause: " + e.getMessage());
//                    return null;
//                }
//            case "java.sql.Date":
//                try {
//                    return (C) Date.valueOf(json);
//                } catch (Exception e) {
//                    log.error("Failed to parse java.sql.Date " + json + " by data format, cause: " + e.getMessage());
//                    return null;
//                }
//            case "java.sql.Timestamp":
//                try {
//                    return (C) Timestamp.valueOf(json);
//                } catch (Exception e) {
//                    log.error("Failed to parse java.sql.Timestamp " + json + " by data format, cause: " + e.getMessage());
//                    return null;
//                }
        }

        if(java.util.Date.class.isAssignableFrom(c) && json.charAt(0) != '"') {
            C result = null;
            if (c == java.util.Date.class) {
                try {
                    result = (C) DATE_FORMAT.parse(json);
                } catch (ParseException e) {
                    log.error("Failed to parse java.util.Date " + json + " by data format, cause: " + e.getMessage());
                }
            }
            else if (c == Time.class) {
                result = (C) Time.valueOf(json);
            }
            else if (c == Date.class) {
                result = (C) Date.valueOf(json);
            }
            else if (c == Timestamp.class) {
                result = (C) Timestamp.valueOf(json);
            }

            if(null == result) {
                json = "\"".concat(json).concat("\"");
            } else {
                return result;
            }
        }

//        if(java.util.Date.class.isAssignableFrom(c)) {
//            try {
//                return (C) DATE_FORMAT.parse(json);
//            } catch (ParseException e) {
//                log.error("Failed to parse date " + json + " by data format, cause: " + e.getMessage());
//                return null;
//            }
//        }

        try {
            return mapper.readValue(json, c);
        } catch (IOException e) {
            log.error("Deserialization " + json + " to Object Error: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(DATE_FORMAT.parse("2018-12-12 12:00:00"));
        System.out.println(DATE_FORMAT.parse("\"2018-12-12 12:00:00\""));
    }
    public static <C> C[] toArray(String json, Class<C[]> clazz) {
        C[] array;
        try {
            array = mapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("Deserialization " + json + " to Array Error: " + e.getMessage());
            return null;
        }
        return array;
    }

    public static <C> List<C> toList(String json, Class<C> clazz) {
        List<C> list;
        try {
            list = mapper.readValue(json, collectionTypeGenerator(clazz));
        } catch (IOException e) {
            log.error("Deserialization " + json + " to List Error: " + e.getMessage());
            return null;
        }
        return list;
    }

    public static <T, U> Map<T, U> toMap(String json, Class<T> t, Class<U> u) {
        Map<T, U> map;
        try {
            map = mapper.readValue(json, mapTypeGenerator(t, u));
        } catch (IOException e) {
            log.error("Deserialization " + json + " to Map Error: " + e.getMessage());
            return null;
        }
        return map;
    }

    protected static CollectionType collectionTypeGenerator(Class<?> cls) {
        String name = cls.getName().intern();
        CollectionType type = collectionTypeMap.get(name);
        if(null == type) {
            type = typeFactory.constructCollectionType(ArrayList.class, cls);
            collectionTypeMap.put(name, type);
        }
        return type;
    }

    protected static MapType mapTypeGenerator(Class<?> t, Class<?> u) {
        String name = (t.getName()+u.getName()).intern();
        MapType type = mapTypeMap.get(name);
        if(null == type) {
            type = typeFactory.constructMapType(HashMap.class, t, u);
            mapTypeMap.put(name, type);
        }
        return type;
    }
}