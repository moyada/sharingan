package cn.moyada.faker.common.utils;

import java.lang.ref.SoftReference;
import java.util.Map;

/**
 * @author xueyikang
 * @create 2018-05-22 21:48
 */
public class SoftReferenceUtil {

    public static <K, V> V get(Map<K, SoftReference<V>> map, K key) {
        SoftReference<V> reference = map.get(key);
        if(null == reference) {
            return null;
        }
        return reference.get();
    }

    public static <K, V> void put(Map<K, SoftReference<V>> map, K key, V value) {
        map.put(key, new SoftReference<>(value));
    }
}
