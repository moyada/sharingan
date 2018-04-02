package cn.moyada.dubbo.faker.core.utils;

import cn.moyada.dubbo.faker.core.model.ParamMapping;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xueyikang
 * @create 2017-12-30 18:48
 */
public class ParamUtil {

    private static final String typeRegex = "_?\\d+\\.\\w+";
    private static final String expressionRegex = "\\$\\{_?\\d+\\.\\w+(\\.\\w+)*\\}";

    /**
     * 查询包含的表达式
     * @param array
     * @return
     */
    public static ParamMapping getRebuildParam(Object[] array) {
        Pattern p1 = Pattern.compile(expressionRegex);
        Pattern p2 = Pattern.compile(typeRegex);

        int length = array.length;

        // 返回参数所含表达式集合
        Map<Integer, ParamMapping.Mapping> rebuildParamMap = Maps.newHashMapWithExpectedSize(length);

        // 返回参数所含查询集合
        Set<String> rebuildParamSet = Sets.newHashSetWithExpectedSize(length);

        Matcher m1, m2;
        String find, mapping;

        ParamMapping.Mapping paramMapping;
        for (int index = 0; index < length; index++) {
            // 查找表达式是否存在
            m1 = p1.matcher(array[index].toString());
            paramMapping = new ParamMapping.Mapping();
            for(; m1.find(); ) {
                find = m1.group();
                // 查找表达式
                m2 = p2.matcher(find);
                if(!m2.find()) {
                    continue;
                }
                mapping = m2.group();
                // 添加查询type
                rebuildParamSet.add(mapping);
                paramMapping.put(find, mapping);
            }
            rebuildParamMap.put(index, paramMapping);
        }

        ParamMapping paramMappingProvider = new ParamMapping();
        paramMappingProvider.setRebuildParamSet(rebuildParamSet);
        paramMappingProvider.setRebuildParamMap(rebuildParamMap);
        return paramMappingProvider;
    }

    public static String toString(Object[] array) {
        if(null == array) {
            return "[]";
        }
        int iMax = array.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(JsonUtil.toJson(array[i]));
            if (i == iMax) {
                return b.append(']').toString();
            }
            b.append(", ");
        }
    }
}
