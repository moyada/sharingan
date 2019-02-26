package io.moyada.sharingan.serialization.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 序列化处理器
 * @author xueyikang
 * @since 1.0
 **/
public interface Serializer {

    /**
     * 序列化
     * @param obj
     * @return
     * @throws SerializationException
     */
    String toString(Object obj) throws SerializationException;

    /**
     * 序列化集合
     * @param list
     * @return
     * @throws SerializationException
     */
    String toString(List<?> list) throws SerializationException;

    /**
     * 反序列化
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws SerializationException
     */
    <T> T toObject(String json, Class<T> clazz) throws SerializationException;

    /**
     * 反序列化数组
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws SerializationException
     */
    <T> T[] toArray(String json, Class<T[]> clazz) throws SerializationException;

    /**
     * 反序列化集合
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws SerializationException
     */
    <T> Collection<T> toList(String json, Class<T> clazz) throws SerializationException;

    /**
     * 反序列化 map
     * @param json
     * @param t
     * @param u
     * @param <T>
     * @param <U>
     * @return
     * @throws SerializationException
     */
    <T, U> Map<T, U> toMap(String json, Class<T> t, Class<U> u) throws SerializationException;
}
