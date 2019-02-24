package io.moyada.sharingan.serialization.jackson;

import io.moyada.sharingan.serialization.api.AbstractSerializer;
import io.moyada.sharingan.serialization.api.Serializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
//        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        //设置JSON时间格式

        mapper.setDateFormat(DATE_FORMAT);
    }

    @Override
    public String toString(Object obj) throws JsonProcessingException {
        if (null == obj) {
            return null;
        }

        Class<?> clazz = obj.getClass();
        if (clazz.isPrimitive() || clazz == String.class) {
            return obj.toString();
        }

        return mapper.writeValueAsString(obj);
    }

    @Override
    public String toString(List<?> list) throws JsonProcessingException {
        return mapper.writeValueAsString(list);
    }

    @Override
    public <T> T toObject(String json, Class<T> clazz) throws IOException {
        if (isEmpty(json)) {
            return null;
        }

        T result = toPrimitiveType(json, clazz);
        if (null != result) {
            return result;
        }

        if(java.util.Date.class.isAssignableFrom(clazz)) {
            if (json.charAt(0) != '"') {
                result = toDataType(json, clazz);
                if (null != result) {
                    return result;
                }
            } else {
                json = "\"".concat(json).concat("\"");
            }
        }

        return mapper.readValue(json, clazz);
    }


    public <T> T[] toArray(String json, Class<T[]> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }

    public <T> List<T> toList(String json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, collectionTypeGenerator(clazz));
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

    public <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) throws IOException {
        return mapper.readValue(json, mapTypeGenerator(kClass, vClass));
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

    public static void main(String[] args) throws Exception {
        Serializer jacksonSerializer = new JacksonSerializer();
        Timestamp timestamp = jacksonSerializer.toObject("\"2018-11-11 12:31:23\"", Timestamp.class);
        Date date = jacksonSerializer.toObject("2018-11-11", Date.class);
        Time time = jacksonSerializer.toObject("12:31:23", Time.class);
        java.util.Date date1 = jacksonSerializer.toObject("2018-11-11 12:31:23", java.util.Date.class);
        System.out.println(jacksonSerializer.toObject("67", int.class));
    }
}
