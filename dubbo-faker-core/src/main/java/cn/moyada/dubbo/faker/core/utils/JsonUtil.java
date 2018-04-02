package cn.moyada.dubbo.faker.core.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
            log.error("Serialization Json Error: " + e);
            return null;
        }
        return json;
    }

    @SuppressWarnings("unchecked")
    public static <C> C toObject(String json, Class<C> c) {
        try {
            if(c.isEnum()) {
                Enum anEnum = Enum.valueOf((Class<Enum>) c, json);
                return c.cast(anEnum);
            }

            switch (c.getName()) {

                case "java.lang.String":
                    return (C) json;

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

                default:
                    return mapper.readValue(json, c);
            }
        } catch (IOException e) {
            log.error("Deserialization Object Error: " + e);
            return null;
        }
    }

    public static <C> C[] toArray(String json, Class<C[]> clazz) {
        C[] array;
        try {
            array = mapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("Deserialization ArrayList Error: " + json + e);
            return null;
        }
        return array;
    }

    public static <C> List<C> toList(String json, Class<C> clazz) {
        List<C> list;
        try {
            list = mapper.readValue(json, collectionTypeGenerator(clazz));
        } catch (IOException e) {
            log.error("Deserialization ArrayList Error: " + json + e);
            return null;
        }
        return list;
    }

    public static <T, U> Map<T, U> toMap(String json, Class<T> t, Class<U> u) {
        Map<T, U> map;
        try {
            map = mapper.readValue(json, mapTypeGenerator(t, u));
        } catch (IOException e) {
            log.error("Deserialization HashMap Error: " + json + e);
            return null;
        }
        return map;
    }

    public static <T, U> Map<T, List<U>> toMapList(String json, Class<T> t, Class<U> u) {
        Map<T, List<U>> map;
        try {
            map = mapper.readValue(json, mapListTypeGenerator(t, u));
        } catch (IOException e) {
            log.error("Deserialization HashMapList Error: " + json + e);
            return null;
        }
        return map;
    }

    public static <T, K, V> Map<T, Map<K, V>> toMapMap(String json, Class<T> t,
                                                       Class<K> k, Class<V> v) {
        Map<T, Map<K, V>> map;
        try {
            map = mapper.readValue(json, mapMapTypeGenerator(t, k, v));
        } catch (IOException e) {
            log.error("Deserialization HashMap Error: " + json + e);
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

    protected static MapType mapListTypeGenerator(Class<?> t, Class<?> u) {
        String name = ("m1-"+t.getName()+u.getName()).intern();
        MapType type = mapTypeMap.get(name);
        if(null == type) {
            JavaType tType = typeFactory.constructType(t);
            JavaType uType = typeFactory.constructCollectionType(List.class, u);
            type = typeFactory.constructMapType(HashMap.class, tType, uType);
            mapTypeMap.put(name, type);
        }
        return type;
    }

    protected static MapType mapMapTypeGenerator(Class<?> t, Class<?> k, Class<?> v) {
        String name = ("m2-"+t.getName()+k.getName()+v.getName()).intern();
        MapType type = mapTypeMap.get(name);
        if(null == type) {
            JavaType tType = typeFactory.constructType(t);
            JavaType uType = typeFactory.constructMapType(HashMap.class, k, v);
            type = typeFactory.constructMapType(HashMap.class, tType, uType);
            mapTypeMap.put(name, type);
        }
        return type;
    }
}