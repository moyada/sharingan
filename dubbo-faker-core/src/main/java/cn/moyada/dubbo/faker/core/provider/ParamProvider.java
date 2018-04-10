package cn.moyada.dubbo.faker.core.provider;

import cn.moyada.dubbo.faker.core.common.BeanHolder;
import cn.moyada.dubbo.faker.core.enums.ConvertType;
import cn.moyada.dubbo.faker.core.exception.InitializeInvokerException;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.ParamMapping;
import cn.moyada.dubbo.faker.core.utils.ConvertUtil;
import cn.moyada.dubbo.faker.core.utils.JsonUtil;
import cn.moyada.dubbo.faker.core.utils.ParamUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 参数提供器
 * @author xueyikang
 * @create 2018-03-27 10:39
 */
public class ParamProvider {

    // 参数表达式
    private final Object[] invokeValue;
    // 参数实际类型
    private Class<?>[] paramTypes;
    private final int length;

    // 参数映射规则
    private Map<Integer, ParamMapping.Mapping> rebuildParamMap;
    // 参数类型映射
    private Map<Integer, ConvertType> convertMap;

    // 参数类型数据集合
    private Map<String, List<String>> fakerParamMap;
    // 参数类型下标提供器
    private Map<String, AbstractIndexProvider> indexProviderMap;

    // 参数映射链
    private Map<String, String[]> linkMap;

    public ParamProvider(Object[] invokeValue, Class<?>[] paramTypes, boolean random) {
        ParamMapping paramMapping = ParamUtil.getRebuildParam(invokeValue);

        if(paramMapping.getRebuildParamSet().isEmpty()) {
            this.length = -1;
        }
        else {
            // 获取参数的替换目标
            this.rebuildParamMap = paramMapping.getRebuildParamMap();

            // 获取替换数据
            FakerManager fakerManager = BeanHolder.getBean(FakerManager.class);
            this.fakerParamMap = fakerManager.getFakerParamMapByRebuildParam(paramMapping.getRebuildParamSet());

            // 获取参数的转换类型
            this.convertMap = ConvertUtil.getConvertMap(paramTypes);

            this.paramTypes = paramTypes;
            this.length = invokeValue.length;

            genParamLinkAndIndex(random);
        }

        this.invokeValue = invokeValue;
    }

    /**
     * 初始化复杂参数表达式映射链
     */
    private void genParamLinkAndIndex(boolean random) {
        linkMap = new HashMap<>();
        indexProviderMap = new HashMap<>();

        String key, value;
        String[] paramLink;
        ParamMapping.Mapping paramMap;

        AbstractIndexProvider indexProvider;
        for (int index = 0; index < length; index++) {

            // 获取当前位置的替换参数
            paramMap = rebuildParamMap.get(index);
            if(null == paramMap) {
                continue;
            }

            for(Map.Entry<String, ParamMapping.TypeCount> entry : paramMap.getParamMap().entrySet()) {

                // 参数中的表达式
                key = entry.getKey();
                // 提取参数的目标
                value = entry.getValue().getType();

                if(random) {
                    indexProvider = new RandomIndexProvider(fakerParamMap.get(value).size());
                }
                else {
                    indexProvider = new OrderIndexProvider(fakerParamMap.get(value).size());
                }
                indexProviderMap.putIfAbsent(value, indexProvider);

                // 替换表达式
                paramLink = findParamLink(key, value);
                if(null != paramLink) {
                    linkMap.put(key, paramLink);
                }
            }
        }
    }

    /**
     * 获取下一个实际参数
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object[] fetchNextParam() {
        if(0 == length) {
            return null;
        }

        if(-1 == length) {
            return invokeValue;
        }

        Object[] argsValue = new Object[length];

        String json, key, value, fakerValue;
        ParamMapping.TypeCount typeCount;
        Object jsonObj;
        String[] paramLink;
        Map<String, Object> jsonMap;
        ParamMapping.Mapping  paramMap;
        List<String> fakerValueList;

        for (int index = 0; index < length; index++) {

            json = JsonUtil.toJson(invokeValue[index]);
            if(null == json) {
                argsValue[index] = null;
                continue;
            }

            // 获取当前位置的替换参数
            paramMap = rebuildParamMap.get(index);
            if(null == paramMap || paramMap.getParamMap().isEmpty()) {
                // 根据参数类型转换
                argsValue[index] = convert(json, convertMap.get(index), paramTypes[index]);
                continue;
            }

//            json = json.replaceFirst(".", "\\.");
            for(Map.Entry<String, ParamMapping.TypeCount> entry : paramMap.getParamMap().entrySet()) {
                // 参数中的表达式
                key = entry.getKey();
                // 提取参数的目标
                typeCount = entry.getValue();
                value = typeCount.getType();

                // 获取模拟参数集合
                fakerValueList = fakerParamMap.get(value);

                // 替换表达式
                paramLink = linkMap.get(key);
                for (int i = 0; i < typeCount.getCount(); i++) {
                    // 获取模拟参数集合
                    fakerValue = fakerValueList.get(indexProviderMap.get(value).nextIndex());

                    if (null == paramLink) {
                        // 替换表达式
                        json = replaceFirst(json, key, fakerValue);
                    } else {
                        // 存在对参数的复杂替换
                        jsonMap = JsonUtil.toMap(fakerValue, String.class, Object.class);

                        // 查询json对象参数
                        int linkIndex = 0;
                        for (; linkIndex < paramLink.length - 1; linkIndex++) {
                            if (null == jsonMap) {
                                // TODO throws exception?
                                json = replaceFirst(json, key, "");
                                break;
                            }
                            jsonMap = (Map<String, Object>) jsonMap.get(paramLink[linkIndex]);
                        }

                        if (null == jsonMap) {
                            // TODO throws exception?
                            json = replaceFirst(json, key, "");
                        } else {
                            jsonObj = jsonMap.get(paramLink[linkIndex]);

                            // 替换表达式
                            value = JsonUtil.toJson(jsonObj);
                            if (null == value) {
                                throw new InitializeInvokerException("数据序列化失败: " + jsonObj.toString());
                            }
                            json = replaceFirst(json, key, value);
                        }
                    }
                }
                argsValue[index] = convert(json, convertMap.get(index), paramTypes[index]);
            }
        }
        return argsValue;
    }

    private static String replaceFirst(String str, String target, String replacement) {
        int index = str.indexOf(target);
        if(-1 == index) {
            return str;
        }
        int length = str.length();
        int eleLength = target.length();
        if(length == eleLength) {
            return replacement;
        }
        char[] chars = new char[length + eleLength];
        int pos = 0;

        index--;
        for (int index1 = 0; index1 <= index; index1++) {
            chars[pos++] = str.charAt(index1);
        }

        eleLength = replacement.length();
        for (int index1 = 0; index1 < eleLength; index1++) {
            chars[pos++] = replacement.charAt(index1);
        }

        for (int index1 = index + target.length() + 1; index1 < length; index1++) {
            chars[pos++] = str.charAt(index1);
        }
        return new String(chars, 0, pos);
        // return (str.substring(0, index) + replacement + str.substring(index + length)).intern();
    }

    /**
     * 根据参数类型转换
     * @param json
     * @param convertType
     * @param paramType
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T convert(String json, ConvertType convertType, Class<T> paramType) {
        // 根据参数类型转换
//        if(ConvertType.OBJECT == convertType) {
//            return JsonUtil.toObject(json, paramType);
//        }

        if(ConvertType.LIST == convertType) {
            return (T) JsonUtil.toList(json, paramType);
        }
        return JsonUtil.toObject(json, paramType);
//        return (T) JsonUtil.toArray(json, paramType);
    }

    /**
     * 获取多级替换
     * @param expression
     * @param paramType
     * @return
     */
    protected static String[] findParamLink(String expression, String paramType) {
        int expressionLen = expression.length();
        int paramTypeLen = paramType.length();

        // 替换目标与参数表达式目标一致
        if(paramTypeLen + 4 > expressionLen) {
            return null;
        }
        // 获取下级替换
        String link = expression.substring(paramTypeLen + 3, expressionLen - 1);
        return link.split("\\.");
    }
}
