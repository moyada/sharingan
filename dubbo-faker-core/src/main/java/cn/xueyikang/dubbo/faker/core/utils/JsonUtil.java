package cn.xueyikang.dubbo.faker.core.utils;

import cn.xueyikang.dubbo.faker.core.parser.JacksonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xueyikang on 2017/8/6.
 */
public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JacksonParser.class);

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

    private static final TypeFactory typeFactory = mapper.getTypeFactory();

    private static final Map<String, ArrayType> arrayTypeMap = new ConcurrentHashMap<>(32);
    private static final Map<String, CollectionType> collectionTypeMap = new ConcurrentHashMap<>(32);
    private static final Map<String, MapType> mapTypeMap = new ConcurrentHashMap<>(32);

    private static final Gson gson = new GsonBuilder().create();
    private static final JsonParser parser = new JsonParser();

    public static String toGsonJson(Object object) {
        return gson.toJson(object);
    }

    public static String toJson(Object object) {
        try {
            if(object instanceof String ||
                    object instanceof Boolean ||
                    object instanceof Number ||
                    object instanceof Character) {
                return object.toString();
            }
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <C> C toObject(String json, Class<C> c) {
        try {
            if(c.isEnum()) {
                Enum anEnum = Enum.valueOf((Class<Enum>) c, json);
                return c.cast(anEnum);
            }
            if("String".equals(c.getSimpleName())) {
                return c.cast(json);
            }
            if("Integer".equals(c.getSimpleName())) {
                return (C) Integer.valueOf(json);
            }
            if("int".equals(c.getSimpleName())) {
                return (C) Integer.valueOf(json);
            }
            if("Long".equals(c.getSimpleName())) {
                return (C) Long.valueOf(json);
            }
            if("long".equals(c.getSimpleName())) {
                return (C) Long.valueOf(json);
            }
            if("Short".equals(c.getSimpleName())) {
                return (C) Short.valueOf(json);
            }
            if("short".equals(c.getSimpleName())) {
                return (C) Short.valueOf(json);
            }
            if("Double".equals(c.getSimpleName())) {
                return (C) Double.valueOf(json);
            }
            if("double".equals(c.getSimpleName())) {
                return (C) Double.valueOf(json);
            }
            if("Float".equals(c.getSimpleName())) {
                return (C) Float.valueOf(json);
            }
            if("float".equals(c.getSimpleName())) {
                return (C) Float.valueOf(json);
            }
            if("Character".equals(c.getSimpleName())) {
                return c.cast(json.charAt(0));
            }
            if("char".equals(c.getSimpleName())) {
                return (C) (Character) json.charAt(0);
            }
            if("Boolean".equals(c.getSimpleName())) {
                return (C) Boolean.valueOf(json);
            }
            if("boolean".equals(c.getSimpleName())) {
                return (C) Boolean.valueOf(json);
            }
            if("Byte".equals(c.getSimpleName())) {
                return (C) Byte.valueOf(json);
            }
            if("byte".equals(c.getSimpleName())) {
                return (C) Byte.valueOf(json);
            }
            return mapper.readValue(json, c);
        } catch (IOException e) {
            log.error("Deserialization Object Error: " + e);
            return null;
        }
    }

    public static  <C> C[] toArray(String json, Class<C> clazz) {
        C[] array;
        try {
            array = mapper.readValue(json, arrayTypeGenerator(clazz));
        } catch (IOException e) {
            log.error("Deserialization Array Error: " + e);
            return null;
        }
        return array;
    }

    public static  <C> List<C> toList(String json, Class<C> clazz) {
        List<C> list;
        try {
            list = mapper.readValue(json, collectionTypeGenerator(clazz));
        } catch (IOException e) {
            log.error("Deserialization List Error: " + e);
            return null;
        }
        return list;
    }

    public static <T, U> Map<T, U> toMap(String json, Class<T> t, Class<U> u) {
        Map<T, U> map;
        try {
            map = mapper.readValue(json, mapTypeGenerator(t, u));
        } catch (IOException e) {
            log.error("Deserialization HashMap Error: " + e);
            return null;
        }
        return map;
    }

    protected static ArrayType arrayTypeGenerator(Class<?> cls) {
        String name = cls.getName().trim();
        ArrayType type = arrayTypeMap.get(name);
        if(null == type) {
            type = typeFactory.constructArrayType(cls);
            arrayTypeMap.put(name, type);
        }
        return type;
    }

    protected static CollectionType collectionTypeGenerator(Class<?> cls) {
        String name = cls.getName().trim();
        CollectionType type = collectionTypeMap.get(name);
        if(null == type) {
            type = typeFactory.constructCollectionType(ArrayList.class, cls);
            collectionTypeMap.put(name, type);
        }
        return type;
    }

    protected static MapType mapTypeGenerator(Class<?> t, Class<?> u) {
        String name = (t.getName().trim()+u.getName().intern()).intern();
        MapType type = mapTypeMap.get(name);
        if(null == type) {
            type = typeFactory.constructMapType(HashMap.class, t, u);
            mapTypeMap.put(name, type);
        }
        return type;
    }

    protected static MapType mapListTypeGenerator(Class<?> t, Class<?> u) {
        String name = ("m1-"+t.getName().trim()+u.getName().intern()).intern();
        MapType type = mapTypeMap.get(name);
        if(null == type) {
            JavaType tType = typeFactory.constructType(t);
            JavaType uType = typeFactory.constructCollectionType(List.class, u);
            type = typeFactory.constructMapType(HashMap.class, tType, uType);
            mapTypeMap.put(name, type);
        }
        return type;
    }

    protected static MapType linkmapListTypeGenerator(Class<?> t, Class<?> u) {
        String name = ("m2-"+t.getName().trim()+u.getName().intern()).intern();
        MapType type = mapTypeMap.get(name);
        if(null == type) {
            JavaType tType = typeFactory.constructType(t);
            JavaType uType = typeFactory.constructCollectionType(List.class, u);
            type = typeFactory.constructMapType(LinkedHashMap.class, tType, uType);
            mapTypeMap.put(name, type);
        }
        return type;
    }
}
