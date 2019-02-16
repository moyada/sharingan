package io.moyada.sharingan.expression;


import io.moyada.sharingan.domain.expression.ParamProvider;
import io.moyada.sharingan.domain.expression.ProviderFactory;
import io.moyada.sharingan.domain.expression.DataRepository;
import io.moyada.sharingan.domain.metadada.MetadataRepository;
import io.moyada.sharingan.expression.provider.*;
import io.moyada.sharingan.expression.range.Range;
import io.moyada.sharingan.expression.range.RangeAnalyser;
import io.moyada.sharingan.infrastructure.ContextFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExpressionProviderFactory implements ProviderFactory {

    @Autowired
    private ContextFactory contextFactory;

    /**
     * 生成参数提供者
     * @param params
     * @param paramTypes
     * @param isRandom
     * @return
     */
    public ParamProvider getParamProvider(String[] params, Class<?>[] paramTypes, boolean isRandom) {
        if (null == params) {
            return ParamProvider.EMPTY_PARAM;
        }

        int length = params.length;
        if (0 == length) {
            return ParamProvider.EMPTY_PARAM;
        }

        ArgsProvider[] providers = new ArgsProvider[length];
        for (int index = 0; index < length; index++) {
            providers[index] = wrapperProvider(params[index], paramTypes[index], isRandom);
        }

        return new ExpressionParamProvider(providers);
    }

    private ArgsProvider wrapperProvider(String param, Class<?> paramType, boolean isRandom) {
        List<ArgsProvider> providers = getProviders(param, paramType, isRandom);

        int size = providers.size();
        if (size == 0) {
            return new ConstantProvider(param, paramType);
        }

        if (size == 1) {
            return providers.get(0);
        }

        return new ComplexProvider(param, paramType, providers.toArray(new ArgsProvider[size]));
    }

    private List<ArgsProvider> getProviders(String param, Class<?> paramType, boolean isRandom) {
        List<ArgsProvider> providers = new ArrayList<>();

        String expression;
        while (true) {
            // 查询替换表达式
            expression = ExpressionAnalyser.findExpression(param);
            if (null != expression) {
                String[] route = ExpressionAnalyser.findRoute(expression);
                if (null != route) {
                    providers.add(new ResourceReplaceProvider(param, paramType, expression, isRandom,
                            getMetadataRepository(), getDataRepository(), route[0], route[1]));
                    param = clearTarget(param, expression);
                    continue;
                }
            }

            // 查询整数表达式
            expression = ExpressionAnalyser.findInt(param);
            if (null != expression) {
                Range<Integer> numberRange = RangeAnalyser.findIntRange(expression);
                if (numberRange.isConstant()) {
                    providers.add(new ConstantReplaceProvider(param, expression, numberRange.getStart(), paramType));
                }
                else {
                    providers.add(new NumberProvider(param, paramType, expression, numberRange));
                }
                param = clearTarget(param, expression);
                continue;
            }

            // 查询浮点数表达式
            expression = ExpressionAnalyser.findDouble(param);
            if (null != expression) {
                Range<Double> decimalRange = RangeAnalyser.findDoubleRange(expression);
                if (decimalRange.isConstant()) {
                    providers.add(new ConstantReplaceProvider(param, expression, decimalRange.getStart(), paramType));
                }
                else {
                    providers.add(new DecimalProvider(param, paramType, expression, decimalRange));
                }
                param = clearTarget(param, expression);
                continue;
            }

            break;
        }
        return providers;
    }

    private MetadataRepository getMetadataRepository() {
        String className = System.getProperty(MetadataRepository.class.getName());
        return contextFactory.getBean(MetadataRepository.class, className);
    }

    private DataRepository getDataRepository() {
        String className = System.getProperty(DataRepository.class.getName());
        return contextFactory.getBean(DataRepository.class, className);
    }

    private String clearTarget(String source, String target) {
        return StringUtils.replaceOnce(source, target, "");
    }
}
