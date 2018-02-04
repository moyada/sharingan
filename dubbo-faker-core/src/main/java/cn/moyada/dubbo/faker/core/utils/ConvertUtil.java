package cn.moyada.dubbo.faker.core.utils;

import cn.moyada.dubbo.faker.core.enums.ConvertType;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;

/**
 * @author xueyikang
 * @create 2017-12-30 16:25
 */
public class ConvertUtil {

    public static Map<Integer, ConvertType> getConvertMap(Class<?>[] paramTypes) {
        int length = paramTypes.length;
        if(0 == length) {
            return Collections.emptyMap();
        }

        Map<Integer, ConvertType> convertMap = Maps.newHashMapWithExpectedSize(length);
        for (int index = 0; index < length; index++) {
            if(paramTypes[index].getSimpleName().endsWith("List")) {
                convertMap.put(index, ConvertType.LIST);
            }
//            if(paramTypes[index].isArray()) {
//                convertMap.put(index, 2);
//            }
            else {
                convertMap.put(index, ConvertType.OBJECT);
            }
        }
        return convertMap;
    }
}
