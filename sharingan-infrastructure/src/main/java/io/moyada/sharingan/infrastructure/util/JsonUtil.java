package io.moyada.sharingan.infrastructure.util;

import io.moyada.sharingan.serialization.api.SerializationException;
import io.moyada.sharingan.serialization.api.Serializer;
import io.moyada.sharingan.serialization.gson.GsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * Created by xueyikang on 2017/8/6.
 */
public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static final Serializer SERIALIZER = new GsonSerializer();

    public static String toJson(Object obj) {
        String json;
        try {
            json = SERIALIZER.toString(obj);
        } catch (SerializationException e) {
            log.error("Serialization json Error: " + e.getMessage());
            return null;
        }
        return json;
    }

    public static <C> C toObject(String json, Class<C> c) {
        try {
            return SERIALIZER.toObject(json, c);
        } catch (SerializationException e) {
            log.error("Deserialization " + json + " to Object Error: " + e.getMessage());
            return null;
        }
    }

    public static <C> C[] toArray(String json, Class<C[]> clazz) {
        try {
            return SERIALIZER.toArray(json, clazz);
        } catch (SerializationException e) {
            log.error("Deserialization " + json + " to Array Error: " + e.getMessage());
            return null;
        }
    }

    public static <C> Collection<C> toList(String json, Class<C> clazz) {
        try {
            return SERIALIZER.toList(json, clazz);
        } catch (SerializationException e) {
            log.error("Deserialization " + json + " to List Error: " + e.getMessage());
            return null;
        }
    }

    public static <T, U> Map<T, U> toMap(String json, Class<T> t, Class<U> u) {
        try {
            return SERIALIZER.toMap(json, t, u);
        } catch (SerializationException e) {
            log.error("Deserialization " + json + " to Map Error: " + e.getMessage());
            return null;
        }
    }
}