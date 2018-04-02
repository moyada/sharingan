package cn.moyada.dubbo.faker.core.provider;

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
 * @author xueyikang
 * @create 2018-03-27 10:39
 */
public class ParamProvider {

    private final Object[] invokeValue;
    private final int length;

    private Map<Integer, Map<String, String>> rebuildParamMap;
    private Map<String, List<String>> fakerParamMap;
    private Map<Integer, ConvertType> convertMap;

    private Class<?>[] paramTypes;

//    private Map<String, Object> jsonMap;
    private Map<String, AbstractIndexProvider> indexProviderMap;

//    private Random random;

    private Map<String, String[]> linkMap;

    public ParamProvider(FakerManager fakerManager, Object[] invokeValue, Class<?>[] paramTypes, boolean random) {
        ParamMapping paramMapping = ParamUtil.getRebuildParam(invokeValue);

        if(paramMapping.getRebuildParamSet().isEmpty()) {
            this.length = -1;
        }
        else {
            // 获取参数的替换目标
            this.rebuildParamMap = paramMapping.getRebuildParamMap();

            // 获取替换数据
            this.fakerParamMap = fakerManager.getFakerParamMapByRebuildParam(paramMapping.getRebuildParamSet());

            // 获取参数的转换类型
            this.convertMap = ConvertUtil.getConvertMap(paramTypes);

            this.paramTypes = paramTypes;
//            this.jsonMap = Maps.newHashMap();
//            this.random = new Random();
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
        Map<String, String> paramMap;

        AbstractIndexProvider indexProvider;
        for (int index = 0; index < length; index++) {

            // 获取当前位置的替换参数
            paramMap = rebuildParamMap.get(index);
            if(null == paramMap) {
                continue;
            }

            for(Map.Entry<String, String> entry : paramMap.entrySet()) {

                // 参数中的表达式
                key = entry.getKey();
                // 提取参数的目标
                value = entry.getValue();

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
        Object jsonObj;
        String[] paramLink;
        Map<String, Object> jsonMap;
        Map<String, String> paramMap;
        List<String> fakerValueList;

        for (int index = 0; index < length; index++) {

            json = JsonUtil.toJson(invokeValue[index]);
            if(null == json) {
                argsValue[index] = null;
                continue;
            }

            // 获取当前位置的替换参数
            paramMap = rebuildParamMap.get(index);
            if(null == paramMap) {
                // 根据参数类型转换
                argsValue[index] = convert(json, convertMap.get(index), paramTypes[index]);
                continue;
            }

            for(Map.Entry<String, String> entry : paramMap.entrySet()) {
                // 参数中的表达式
                key = entry.getKey();
                // 提取参数的目标
                value = entry.getValue();

                // 获取模拟参数集合
                fakerValueList = fakerParamMap.get(value);

//                fakerValue = fakerValueList.get(random.nextInt(fakerValueList.size()));
                fakerValue = fakerValueList.get(indexProviderMap.get(value).nextIndex());

                // 替换表达式
                paramLink = linkMap.get(key);
                if(null == paramLink) {
//                    // 获取模拟参数集合
//                    fakerValueList = fakerParamMap.get(value);
//
//                    fakerValue = fakerValueList.get(random.nextInt(fakerValueList.size()));
                    // 替换表达式
                    json = json.replace(key, fakerValue);
//                        json = StringUtils.replaceOnce(json, key, fakerValue);
                }
                else {
                    // 存在对参数的复杂替换

//                    jsonObj = jsonMap.get(value);
//                    if(null == jsonObj) {
//                        // 获取模拟参数集合
//                        fakerValueList = fakerParamMap.get(value);
//
//                        fakerValue = fakerValueList.get(random.nextInt(fakerValueList.size()));
//                        jsonObj = JsonUtil.toObject(fakerValue, Object.class);
//                        jsonMap.put(value, jsonObj);
//                    }
                    jsonMap = JsonUtil.toMap(fakerValue, String.class, Object.class);

                    // 查询json对象参数
                    int linkIndex = 0;
                    for (; linkIndex < paramLink.length - 1; linkIndex++) {
                        if(null == jsonMap) {
                            // TODO throws exception?
                            json = json.replace(key, "");
//                            json = StringUtils.replaceOnce(json, key, "");
                            break;
                        }
                        jsonMap = (Map<String, Object>) jsonMap.get(paramLink[linkIndex]);
                    }
                    if(null == jsonMap) {
                        // TODO throws exception?
                        json = json.replace(key, "");
//                        json = StringUtils.replaceOnce(json, key, "");
                    }
                    else {
                        jsonObj = jsonMap.get(paramLink[linkIndex]);

                        // 替换表达式
                        value = JsonUtil.toJson(jsonObj);
                        if(null == value) {
                            throw new InitializeInvokerException("数据序列化失败: " + jsonObj.toString());
                        }
                        json = json.replace(key, value);
//                        json = StringUtils.replaceOnce(json, key, JsonUtil.toJson(jsonObj));
                    }
//                    for (String link : paramLink) {
//                        if(null == jsonObj) {
//                            // TODO throws exception?
//                            json = StringUtils.replaceOnce(json, key, "");
//                            break;
//                        }
//                        jsonObj = ((Map<String, Object>)jsonObj).get(link);
//                    }
//                         替换表达式
//                        json = StringUtils.replaceOnce(json, key, JsonUtil.toJson(jsonObj));
//                    }
                }

                argsValue[index] = convert(json, convertMap.get(index), paramTypes[index]);
            }
        }
        return argsValue;
    }

    /**
     * 根据参数类型转换
     * @param json
     * @param convertType
     * @param paramType
     * @return
     */
    private Object convert(String json, ConvertType convertType, Class<?> paramType) {
        // 根据参数类型转换
        if(ConvertType.OBJECT == convertType) {
            return JsonUtil.toObject(json, paramType);
        }

        if(ConvertType.LIST == convertType) {
            return JsonUtil.toList(json, Object.class);
        }

        return JsonUtil.toArray(json, Object[].class);
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
