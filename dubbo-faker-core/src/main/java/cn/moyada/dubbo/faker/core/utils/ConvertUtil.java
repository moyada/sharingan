package cn.moyada.dubbo.faker.core.utils;

import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;

/**
 * @author xueyikang
 * @create 2017-12-30 16:25
 */
public class ConvertUtil {

    public static Map<Integer, Integer> getConvertMap(Class<?>[] paramTypes) {
        int length = paramTypes.length;
        if(0 == length) {
            return Collections.emptyMap();
        }

        Map<Integer, Integer> convertMap = Maps.newHashMapWithExpectedSize(length);
        for (int index = 0; index < length; index++) {
            if(paramTypes[index].getSimpleName().endsWith("List")) {
                convertMap.put(index, 1);
            }
//            if(paramTypes[index].isArray()) {
//                convertMap.put(index, 2);
//            }
            else {
                convertMap.put(index, 0);
            }
        }
        return convertMap;
    }
}
