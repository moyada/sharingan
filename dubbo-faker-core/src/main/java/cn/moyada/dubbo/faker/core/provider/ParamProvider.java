package cn.moyada.dubbo.faker.core.provider;

import cn.moyada.dubbo.faker.core.enums.ConvertType;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.ParamMapping;
import cn.moyada.dubbo.faker.core.utils.ConvertUtil;
import cn.moyada.dubbo.faker.core.utils.JsonUtil;
import cn.moyada.dubbo.faker.core.utils.ParamUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author xueyikang
 * @create 2018-03-27 10:39
 */
public class ParamProvider {

    private final Map<Integer, Map<String, String>> rebuildParamMap;
    private final Map<String, List<String>> fakerParamMap;
    private final Map<Integer, ConvertType> convertMap;
    private final Object[] invokeValue;
    private final Class<?>[] paramTypes;

    private final Map<String, Object> jsonMap = Maps.newHashMap();
    private final int length;
    private static final Random random = new Random();

    public ParamProvider(FakerManager fakerManager, Object[] invokeValue, Class<?>[] paramTypes) {
        ParamMapping paramMapping = ParamUtil.getRebuildParam(invokeValue);

        // 获取参数的替换目标
        this.rebuildParamMap = paramMapping.getRebuildParamMap();

        // 获取替换数据
        this.fakerParamMap = fakerManager.getFakerParamMapByRebuildParam(paramMapping.getRebuildParamSet());

        // 获取参数的转换类型
        this.convertMap = ConvertUtil.getConvertMap(paramTypes);

        this.invokeValue = invokeValue;
        this.paramTypes = paramTypes;
        this.length = invokeValue.length;
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

        Object[] argsValue = new Object[length];

        String json, key, value, fakerValue;
        String[] paramLink;
        Map<String, String> paramMap;
        Object jsonObj;
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
                continue;
            }

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
                    // 存在对参数的复杂替换

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

                // 根据参数类型转换
                if(ConvertType.OBJECT == convertMap.get(index)) {
                    argsValue[index] = JsonUtil.toObject(json, paramTypes[index]);
                }
                else if(ConvertType.LIST == convertMap.get(index)) {
                    argsValue[index] = JsonUtil.toList(json, Object.class);
                }
                else {
                    argsValue[index] = JsonUtil.toArray(json, Object[].class);
                }
            }
        }
        return argsValue;
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
