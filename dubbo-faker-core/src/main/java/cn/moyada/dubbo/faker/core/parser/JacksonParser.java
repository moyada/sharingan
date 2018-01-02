package cn.moyada.dubbo.faker.core.parser;

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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xueyikang on 2017/12/16.
 */
public class JacksonParser extends JsonParser {
    private static final Logger log = LoggerFactory.getLogger(JacksonParser.class);

    private final ObjectMapper mapper;
    private final TypeFactory typeFactory;

    private final Map<String, CollectionType> collectionTypeMap;
    private final Map<String, MapType> mapTypeMap;

    public JacksonParser() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        this.mapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, true);
        this.mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        this.mapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true);
        this.mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        this.typeFactory = this.mapper.getTypeFactory();
        this.collectionTypeMap = new ConcurrentHashMap<>(32);
        this.mapTypeMap = new ConcurrentHashMap<>(32);
    }

    @Override
    public String toJson(Object obj) {
        String json;
        try {
            json = this.mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Serialization Json Error: " + e);
            return null;
        }
        return json;
    }

    @Override
    public <C> C toObject(String json, Class<C> c) {
        C obj;
        try {
            obj = this.mapper.readValue(json, c);
        } catch (IOException e) {
            log.error("Deserialization Object Error: " + e);
            return null;
        }
        return obj;
    }

    @Override
    public <C> C[] toArray(String json, Class<C> c) {
        return null;
    }

    @Override
    public <C> List<C> toList(String json, Class<C> clazz) {
        List<C> list;
        try {
            list = this.mapper.readValue(json, this.collectionTypeGenerator(clazz));
        } catch (IOException e) {
            log.error("Deserialization ArrayList Error: " + e);
            return null;
        }
        return list;
    }

    @Override
    public <T, U> Map<T, U> toMap(String json, Class<T> t, Class<U> u) {
        Map<T, U> map;
        try {
            map = this.mapper.readValue(json, this.mapTypeGenerator(t, u));
        } catch (IOException e) {
            log.error("Deserialization HashMap Error: " + e);
            return null;
        }
        return map;
    }

    @Override
    public <T, U> LinkedHashMap<T, List<U>> toLinkedMapList(String json, Class<T> t, Class<U> u) {
        LinkedHashMap<T, List<U>> map;
        try {
            map = this.mapper.readValue(json, this.linkmapListTypeGenerator(t, u));
        } catch (IOException e) {
            log.error("Deserialization LinkedHashMapList Error: " + e);
            return null;
        }
        return map;
    }

    @Override
    public <T, U> Map<T, List<U>> toMapList(String json, Class<T> t, Class<U> u) {
        Map<T, List<U>> map;
        try {
            map = this.mapper.readValue(json, this.mapListTypeGenerator(t, u));
        } catch (IOException e) {
            log.error("Deserialization HashMapList Error: " + e);
            return null;
        }
        return map;
    }

    protected CollectionType collectionTypeGenerator(Class<?> cls) {
        String name = cls.getName().trim();
        CollectionType type = this.collectionTypeMap.get(name);
        if(null == type) {
            type = this.typeFactory.constructCollectionType(ArrayList.class, cls);
            this.collectionTypeMap.put(name, type);
        }
        return type;
    }

    protected MapType mapTypeGenerator(Class<?> t, Class<?> u) {
        String name = (t.getName().trim()+u.getName().intern()).intern();
        MapType type = this.mapTypeMap.get(name);
        if(null == type) {
            type = this.typeFactory.constructMapType(HashMap.class, t, u);
            this.mapTypeMap.put(name, type);
        }
        return type;
    }

    protected MapType mapListTypeGenerator(Class<?> t, Class<?> u) {
        String name = ("m1-"+t.getName().trim()+u.getName().intern()).intern();
        MapType type = this.mapTypeMap.get(name);
        if(null == type) {
            JavaType tType = this.typeFactory.constructType(t);
            JavaType uType = this.typeFactory.constructCollectionType(List.class, u);
            type = this.typeFactory.constructMapType(HashMap.class, tType, uType);
            this.mapTypeMap.put(name, type);
        }
        return type;
    }

    protected MapType linkmapListTypeGenerator(Class<?> t, Class<?> u) {
        String name = ("m2-"+t.getName().trim()+u.getName().intern()).intern();
        MapType type = this.mapTypeMap.get(name);
        if(null == type) {
            JavaType tType = this.typeFactory.constructType(t);
            JavaType uType = this.typeFactory.constructCollectionType(List.class, u);
            type = this.typeFactory.constructMapType(LinkedHashMap.class, tType, uType);
            this.mapTypeMap.put(name, type);
        }
        return type;
    }
}
