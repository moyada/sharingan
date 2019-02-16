package io.moyada.sharingan.serialization.fastjson;

import io.moyada.sharingan.serialization.api.AbstractSerializer;
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
    public String toString(Object obj) {
        return JSON.toJSONString(obj);
    }

    @Override
    public String toString(List<?> list) {
        return JSON.toJSONString(list);
    }

    @Override
    public <T> T toObject(String json, Class<T> clazz) throws Exception {
        if (isEmpty(json)) {
            return null;
        }

        T data = toPrimitiveType(json, clazz);
        if (null != data) {
            return data;
        }

        return JSON.parseObject(json, clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(String json, Class<T[]> clazz) throws Exception {
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
    public <T> List<T> toList(String json, Class<T> clazz) throws Exception {
        return JSON.parseArray(json, clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, U> Map<T, U> toMap(String json, Class<T> t, Class<U> u) throws Exception {
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


    public static void main(String[] args) throws Exception {
        Serializer serializer = new FastJsonSerializer();

        Map<String, Integer> map = new HashMap<>();
        map.put("haha", 666);
        map.put("zhangsan", 250);
        String s = serializer.toString(map);
        System.out.println(s);
        System.out.println(serializer.toMap(s, String.class, int.class));
    }
}
