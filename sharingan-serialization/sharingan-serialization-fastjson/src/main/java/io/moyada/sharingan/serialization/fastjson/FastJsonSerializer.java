package io.moyada.sharingan.serialization.fastjson;

import io.moyada.sharingan.serialization.api.AbstractSerializer;
import io.moyada.sharingan.serialization.api.SerializationException;
import io.moyada.sharingan.serialization.api.Serializer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * FastJson 序列化处理器
 * @author xueyikang
 * @since 1.0
 **/
public class FastJsonSerializer extends AbstractSerializer implements Serializer {

    @Override
    public String toString(Object obj) throws SerializationException {
        checkNull(obj);

        return JSON.toJSONString(obj);
    }

    @Override
    public String toString(List<?> list) throws SerializationException {
        checkNull(list);

        return JSON.toJSONString(list);
    }

    @Override
    public <T> T toObject(String json, Class<T> clazz) throws SerializationException {
        checkJson(json);

        return JSON.parseObject(json, clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(String json, Class<T[]> clazz) throws SerializationException {
        checkJson(json);

        JSONArray objects = JSON.parseArray(json);

        int size = objects.size();
        T[] data = (T[]) new Object[size];
        if (0 == size) {
            return data;
        }

        Class<T> type = (Class<T>) clazz.getComponentType();
        for (int i = 0; i < size; i++) {
            data[i] = objects.getObject(i, type);
        }
        return data;
    }

    @Override
    public <T> Collection<T> toList(String json, Class<T> clazz) throws SerializationException {
        checkJson(json);

        return JSON.parseArray(json, clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, U> Map<T, U> toMap(String json, Class<T> t, Class<U> u) throws SerializationException {
        checkJson(json);

        JSONObject jsonObject = JSON.parseObject(json);
        if (jsonObject.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<T, U> data = new HashMap<>();
        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();

        String key;
        Object value;
        T k;
        U v;
        for (Map.Entry<String, Object> entry : entries) {
            key = entry.getKey();
            value = entry.getValue();

            if (t.isInstance(key)) {
                k = (T) key;
            } else {
                k = JSON.parseObject(key, t);
            }


            if (u.isInstance(value)) {
                v = (U) value;
            } else {
                v = JSON.parseObject(JSON.toJSONString(value), u);
            }

            data.put(k, v);
        }

        return data;
    }
}
