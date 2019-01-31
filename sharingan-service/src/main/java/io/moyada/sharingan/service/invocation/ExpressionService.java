package io.moyada.sharingan.service.invocation;


import io.moyada.sharingan.domain.expression.ParamProvider;
import io.moyada.sharingan.domain.expression.ProviderFactory;
import io.moyada.sharingan.domain.metadada.ClassData;
import io.moyada.sharingan.domain.request.QuestInfo;
import io.moyada.sharingan.infrastructure.invoke.data.HttpInvocation;
import io.moyada.sharingan.infrastructure.util.JsonUtil;
import io.moyada.sharingan.infrastructure.util.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Service
public class ExpressionService {

    @Autowired
    private ProviderFactory providerFactory;

    public ParamProvider getMethodParamProvider(QuestInfo questInfo, ClassData classData) {
        String[] expression = JsonUtil.toArray(questInfo.getExpression(), String[].class);
        if (null == expression) {
            throw new NullPointerException();
        }
        return getParamProvider(expression, classData.getParamTypes(), questInfo.getRandom());
    }

    public ParamProvider getHttpParamProvider(QuestInfo questInfo, HttpInvocation httpInvocation) {
        String[] expression = getHttpExpression(questInfo.getExpression(),
                httpInvocation.getMethodName(), httpInvocation.getParam(), httpInvocation.getHeader());
        return getHttpParamProvider(expression, questInfo.getRandom());
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
            throw new IllegalStateException();
        }
        return providerFactory.getParamProvider(expression, paramTypes, isRandom);
    }

    private static String[] getHttpExpression(String inputExpression, String method,
                                              String[] params, String[] headers) {
        List<String> variable = RegexUtil.findPathVariable(method);
        int paramLeng = variable.size();
        if (null != params) {
            paramLeng += params.length;
        }
        int headerLen = 0;
        if (null != headers) {
            headerLen += headers.length;
        }

        int length = paramLeng + headerLen;

        String[] expression = new String[length];

        Map<String, Map> expressionMap = JsonUtil.toMap(inputExpression, String.class, Map.class);
        if (null == expressionMap) {
            for (int index = 0; index < variable.size(); index++) {
                expression[index] = "{" + variable.get(index) + "}";
            }
            for (int index = variable.size(); index < length; index++) {
                expression[index] = "";
            }
            return expression;
        }

        int index = 0;
        Object value;

        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = expressionMap.get("param");
        if (null == paramMap) {
            for (int i = 0; i < paramLeng; i++) {
                expression[index++] = "";
            }
        }
        else {
            for (String item : variable) {
                value = paramMap.get(item);
                expression[index++] = null == value ? "{" + item + "}" : value.toString();
            }
            if (null != params) {
                for (String item : params) {
                    value = paramMap.get(item);
                    expression[index++] = null == value ? "" : value.toString();
                }
            }
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> headerMap = expressionMap.get("header");
        if (null == headerMap) {
            for (int i = 0; i < headerLen; i++) {
                expression[index++] = "";
            }
        }
        else {
            if (null != headers) {
                for (String item : headers) {
                    value = headerMap.get(item);
                    expression[index++] = null == value ? "" : value.toString();
                }
            }
        }

        return expression;
    }
}
