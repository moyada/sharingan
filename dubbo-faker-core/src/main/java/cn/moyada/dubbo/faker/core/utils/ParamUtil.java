package cn.moyada.dubbo.faker.core.utils;

import cn.moyada.dubbo.faker.core.enums.ConvertType;
import cn.moyada.dubbo.faker.core.model.ParamProvider;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xueyikang
 * @create 2017-12-30 18:48
 */
public class ParamUtil {

    private static final String typeRegex = "_?\\d+\\.\\w+";
    private static final String expressionRegex = "\\$\\{_?\\d+\\.\\w+(\\.\\w+)*\\}";
    private static final Random random = new Random();

    /**
     * 查询包含的表达式
     * @param array
     * @return
     */
    public static ParamProvider getRebuildParam(Object[] array) {
        Pattern p1 = Pattern.compile(expressionRegex);
        Pattern p2 = Pattern.compile(typeRegex);

        int length = array.length;

        // 返回参数所含表达式集合
        Map<Integer, Map<String, String>> rebuildParamMap = Maps.newHashMapWithExpectedSize(length);

        // 返回参数所含查询集合
        Set<String> rebuildParamSet = Sets.newHashSetWithExpectedSize(length);

        Matcher m1, m2;
        String find, mapping;
        Map<String, String> param;
        for (int index = 0; index < length; index++) {
            m1 = p1.matcher(array[index].toString());
            param = Maps.newHashMap();
            for(; m1.find(); ) {
                find = m1.group();
                m2 = p2.matcher(find);
                if(!m2.find()) {
                    continue;
                }
                mapping = m2.group();
                param.put(find, mapping);
                rebuildParamSet.add(mapping);
                rebuildParamMap.put(index, param);
            }
        }

        ParamProvider paramProvider = new ParamProvider();
        paramProvider.setRebuildParamSet(rebuildParamSet);
        paramProvider.setRebuildParamMap(rebuildParamMap);
        return paramProvider;
    }

    /**
     * 获取实际参数
     * @param values 原始参数
     * @param paramTypes 参数类型
     * @param rebuildParamMap 参数表达式
     * @param fakerParamMap 模拟参数
     * @param convertMap 转换方式
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object[] fetchParam(Object[] values, Class<?>[] paramTypes, Map<Integer, Map<String, String>> rebuildParamMap,
                                      Map<String, List<String>> fakerParamMap, Map<Integer, ConvertType> convertMap) {
        int length = values.length;
        if(0 == length) {
            return null;
        }

        Object[] argsValue = new Object[length];
        Map<String, Object> jsonMap = Maps.newHashMap();

        String json, key, value, fakerValue;
        String[] paramLink;
        Map<String, String> paramMap;
        Object jsonObj;
        List<String> fakerValueList;

        for (int index = 0; index < length; index++) {

            json = JsonUtil.toJson(values[index]);
            if(null == json) {
                argsValue[index] = null;
                continue;
            }

            paramMap = rebuildParamMap.get(index);
            if(null != paramMap) {
                for(Map.Entry<String, String> entry : paramMap.entrySet()) {
                    // 参数中的表达式
                    key = entry.getKey();
                    // 提取参数的目标
                    value = entry.getValue();

                    // 替换表达式
                    paramLink = findParamLink(key, value);
                    if(null == paramLink) {
                        // 获取模拟参数集合
                        fakerValueList = fakerParamMap.get(value);

                        fakerValue = fakerValueList.get(random.nextInt(fakerValueList.size()));
                        // 替换表达式
                        json = StringUtils.replaceOnce(json, key, fakerValue);
                    }
                    else {
                        jsonObj = jsonMap.get(value);
                        if(null == jsonObj) {
                            // 获取模拟参数集合
                            fakerValueList = fakerParamMap.get(value);

                            fakerValue = fakerValueList.get(random.nextInt(fakerValueList.size()));
                            jsonObj = JsonUtil.toObject(fakerValue, Object.class);
                            jsonMap.put(value, jsonObj);
                        }
                        // 查询json对象参数
                        for (String link : paramLink) {
                            if(null == jsonObj) {
                                json = StringUtils.replaceOnce(json, key, "");
                                break;
                            }
                            jsonObj = ((Map<String, Object>)jsonObj).get(link);
                        }
                        // 替换表达式
                        json = StringUtils.replaceOnce(json, key, JsonUtil.toJson(jsonObj));
                    }
                }
            }

            if(ConvertType.LIST == convertMap.get(index)) {
                argsValue[index] = JsonUtil.toList(json, Object.class);
            }
            else {
                argsValue[index] = JsonUtil.toObject(json, paramTypes[index]);
            }
        }
        return argsValue;
    }

    public static String[] findParamLink(String expression, String paramType) {
        int expressionLen = expression.length();
        int paramTypeLen = paramType.length();
        if(paramTypeLen + 4 > expressionLen) {
            return null;
        }
        String link = expression.substring(paramTypeLen + 3, expressionLen - 1);
        return link.split("\\.");
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(findParamLink("${123.demo}", "123.demo")));


        String json = "{\"info\" : { \"name\": \"test\"}, \"id\" : \"123\"}";
        Object jsonObj = JsonUtil.toObject(json, Object.class);

        jsonObj = ((Map<String, Object>)jsonObj).get("info");
        jsonObj = ((Map<String, Object>)jsonObj).get("name");
        System.out.println(jsonObj);
    }
}
