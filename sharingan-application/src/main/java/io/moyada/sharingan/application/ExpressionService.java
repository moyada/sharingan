package io.moyada.sharingan.application;


import io.moyada.sharingan.domain.expression.ParamProvider;
import io.moyada.sharingan.domain.expression.ProviderFactory;
import io.moyada.sharingan.domain.metadada.ClassData;
import io.moyada.sharingan.domain.request.QuestInfo;
import io.moyada.sharingan.expression.HttpInfo;
import io.moyada.sharingan.infrastructure.invoke.data.HttpInvocation;
import io.moyada.sharingan.infrastructure.util.JsonUtil;
import io.moyada.sharingan.infrastructure.util.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Service
public class ExpressionService {

    private ProviderFactory providerFactory;

    @Autowired
    public ExpressionService(ProviderFactory providerFactory) {
        this.providerFactory = providerFactory;
    }

    public ParamProvider getMethodParamProvider(QuestInfo questInfo, ClassData classData) {
        String[] expression = JsonUtil.toArray(questInfo.getExpression(), String[].class);
        if (null == expression) {
            return ParamProvider.EMPTY_PARAM;
        }
        return getParamProvider(expression, classData.getParamTypes(), questInfo.getRandom());
    }

    public ParamProvider getHttpParamProvider(QuestInfo questInfo, HttpInvocation httpInvocation) {
        HttpInfo httpInfo = getHttpExpression(questInfo.getExpression(), questInfo.getHeader(), questInfo.getBody(), httpInvocation.getMethodName());
        if (httpInfo.isEmpty()) {
            return ParamProvider.EMPTY_PARAM;
        }

        httpInvocation.setParam(httpInfo.getParam());
        httpInvocation.setHeader(httpInfo.getHeader());
        httpInvocation.setHasBody(httpInfo.getBody() != null);
        return getHttpParamProvider(httpInfo.getValue(), questInfo.getRandom());
    }

    private ParamProvider getHttpParamProvider(String[] expression, boolean isRandom) {
        int length = expression.length;
        Class<?>[] classes;
        if (length == 0) {
            classes = new Class[0];
        }
        else {
            classes = new Class[length];
            for (int i = 0; i < length; i++) {
                classes[i] = String.class;
            }
        }
        return getParamProvider(expression, classes, isRandom);
    }

    private ParamProvider getParamProvider(String[] expression, Class<?>[] paramTypes, boolean isRandom) {
        if (expression.length != paramTypes.length) {
            throw new IllegalStateException("expression number error.");
        }
        return providerFactory.getParamProvider(expression, paramTypes, isRandom);
    }

    private static HttpInfo getHttpExpression(String param, String header, String body, String method) {
        Map<String, String> paramMapper = getVariableParam(method);

        Map<String, Object> paramMap = JsonUtil.toMap(param, String.class, Object.class);
        if (null != paramMap) {
            if (null == paramMapper) {
                paramMapper = new HashMap<>(paramMap.size());
            }

            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                paramMapper.put(entry.getKey(), RegexUtil.replaceJson(JsonUtil.toJson(entry.getValue())));
            }
        }

        Map<String, String> headerMapper;
        if (null == header) {
            headerMapper = null;
        } else {
            Map<String, Object> headerMap = JsonUtil.toMap(header, String.class, Object.class);
            if (null == headerMap) {
                headerMapper = null;
            } else {
                headerMapper = new HashMap<>(headerMap.size());
                for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
                    headerMapper.put(entry.getKey(), RegexUtil.replaceJson(JsonUtil.toJson(entry.getValue())));
                }
            }
        }

        return new HttpInfo(paramMapper, headerMapper, body);
    }

    private static Map<String, String> getVariableParam(String method) {
        List<String> variable = RegexUtil.findPathVariable(method);
        if (variable.isEmpty()) {
            return null;
        }

        Map<String, String> param = new HashMap<>();
        for (String var : variable) {
            param.put(var, "{" + var + "}");
        }

        return param;
    }
}
