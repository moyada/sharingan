package cn.moyada.faker.common.utils;


import cn.moyada.faker.common.enums.ConvertType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xueyikang
 * @create 2017-12-30 16:25
 */
public class ConvertUtil {

    public static int[] convertInt(String[] strs) {
        if(null == strs) {
            return null;
        }
        int length = strs.length;
        if(0 == length) {
            return null;
        }
        int[] ints = new int[length];
        int index = 0;
        for (String str : strs) {
            ints[index++] = Integer.valueOf(str);
        }
        return ints;
    }

    /**
     * 获取参数的转换目标
     * @param paramTypes
     * @return
     */
    public static Map<Integer, ConvertType> getConvertMap(Class<?>[] paramTypes) {
        int length = paramTypes.length;
        if(0 == length) {
            return Collections.emptyMap();
        }

        Map<Integer, ConvertType> convertMap = new HashMap<>(length, 1);
        for (int index = 0; index < length; index++) {
            if(paramTypes[index].getName().endsWith("List")) {
                convertMap.put(index, ConvertType.LIST);
            }
            if(paramTypes[index].isArray()) {
                convertMap.put(index, ConvertType.ARRAY);
            }
            else {
                convertMap.put(index, ConvertType.OBJECT);
            }
        }
        return convertMap;
    }
}
