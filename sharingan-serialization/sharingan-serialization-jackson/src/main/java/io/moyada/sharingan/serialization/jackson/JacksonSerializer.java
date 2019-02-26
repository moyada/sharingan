package io.moyada.sharingan.serialization.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.moyada.sharingan.serialization.api.AbstractSerializer;
import io.moyada.sharingan.serialization.api.SerializationException;
import io.moyada.sharingan.serialization.api.Serializer;

import java.io.IOException;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Jackson 序列化处理器
 * @author xueyikang
 * @since 1.0
 **/
public class JacksonSerializer extends AbstractSerializer implements Serializer {

    private final ObjectMapper mapper;
    private final TypeFactory typeFactory;
    private final Map<String, CollectionType> collectionTypeMap;
    private final Map<String, MapType> mapTypeMap;

    public JacksonSerializer() {
        mapper = new ObjectMapper();
        typeFactory = mapper.getTypeFactory();
        collectionTypeMap = new ConcurrentHashMap<>();
        mapTypeMap = new ConcurrentHashMap<>();

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
        mapper.setDateFormat(dateFormat);
    }

    @Override
    public String toString(Object obj) throws SerializationException {
        checkNull(obj);

        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public String toString(List<?> list) throws SerializationException {
        checkNull(list);

        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> T toObject(String json, Class<T> clazz) throws SerializationException {
        checkJson(json);

        if (clazz == Date.class) {
            char first = json.charAt(0);
            int begin = first == '\'' || first == '"' ? 1 : 0;
            int end = json.indexOf(' ');
            end = -1 == end ? json.length() - 1 : end;
            @SuppressWarnings("unchecked")
            T obj = (T) Date.valueOf(json.substring(begin, end));
            return obj;
        }

        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }


    public <T> T[] toArray(String json, Class<T[]> clazz) throws SerializationException {
        checkJson(json);

        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    public <T> Collection<T> toList(String json, Class<T> clazz) throws SerializationException {
        checkJson(json);

        try {
            return mapper.readValue(json, collectionTypeGenerator(clazz));
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    private CollectionType collectionTypeGenerator(Class<?> cls) {
        String name = cls.getName().intern();
        CollectionType type = collectionTypeMap.get(name);
        if(null == type) {
            type = typeFactory.constructCollectionType(ArrayList.class, cls);
            collectionTypeMap.put(name, type);
        }
        return type;
    }

    public <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) throws SerializationException {
        try {
            return mapper.readValue(json, mapTypeGenerator(kClass, vClass));
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    private MapType mapTypeGenerator(Class<?> kClass, Class<?> vClass) {
        String name = (kClass.getSimpleName() + vClass.getSimpleName()).intern();
        MapType type = mapTypeMap.get(name);
        if(null == type) {
            type = typeFactory.constructMapType(HashMap.class, kClass, vClass);
            mapTypeMap.put(name, type);
        }
        return type;
    }
}
