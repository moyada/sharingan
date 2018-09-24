package cn.moyada.sharingan.core.factory;

import cn.moyada.sharingan.core.provider.*;
import cn.moyada.sharingan.core.support.*;
import cn.moyada.sharingan.storage.api.ArgsRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProviderFactory {

    @Autowired
    private RouteProcessor routeProcessor;

    @Autowired
    private ArgsRepository argsRepository;

    /**
     * 生成参数提供者
     * @param params
     * @param paramTypes
     * @param isRandom
     * @return
     */
    public ArgsProviderContainer genArgsProvider(String[] params, Class<?>[] paramTypes, boolean isRandom) {
        if (null == params) {
            return ArgsProviderContainer.emptyContainer();
        }

        int length = params.length;
        if (0 == length) {
            return ArgsProviderContainer.emptyContainer();
        }

        ArgsProvider[] providers = new ArgsProvider[length];
//        String param;
//        RouteInfo route;
//        IntRange intRange;
//        DoubleRange doubleRange;

        for (int index = 0; index < length; index++) {
//            param = params[index];
//            // 查询替换表达式
//            route = routeProcessor.getRoute(param);
//            if (null != route) {
//                providers[index] = new RandomReplaceProvider(param, paramTypes[index], route, argsRepository, isRandom);
//                continue;
//            }
//
//            // 查询整数表达式
//            intRange = routeProcessor.getIntRange(param);
//            if (null != intRange) {
//                if (intRange.isConstant()) {
//                    providers[index] = new ConstantProvider(String.valueOf(intRange.getStart()), paramTypes[index]);
//                }
//                else {
//                    providers[index] = new IntProvider(param, paramTypes[index], intRange);
//                }
//                continue;
//            }
//
//            // 查询浮点数表达式
//            doubleRange = routeProcessor.getDoubleRange(param);
//            if (null != doubleRange) {
//                if (doubleRange.isConstant()) {
//                    providers[index] = new ConstantProvider(String.valueOf(doubleRange.getStart()), paramTypes[index]);
//                }
//                else {
//                    providers[index] = new DoubleProvider(param, paramTypes[index], doubleRange);
//                }
//                continue;
//            }

            providers[index] = getProvider(params[index], paramTypes[index], isRandom);
        }

        ArgsProviderContainer container = ArgsProviderContainer.emptyContainer();
        container.setProviders(providers);
        return container;
    }

    private ArgsProvider getProvider(String param, Class<?> paramType, boolean isRandom) {
        List<ArgsProvider> providers = new ArrayList<>();

        String expression = param;
        RouteInfo route;
        IntRange intRange;
        DoubleRange doubleRange;

        while (true) {
            // 查询替换表达式
            route = routeProcessor.getRoute(expression);
            if (null != route) {
                providers.add(new RandomReplaceProvider(param, paramType, route, argsRepository, isRandom));
                expression = clearTarget(expression, route.getTarget());
                continue;
            }

            // 查询整数表达式
            intRange = routeProcessor.getIntRange(expression);
            if (null != intRange) {
                if (intRange.isConstant()) {
                    providers.add(new ConstantReplaceProvider(String.valueOf(intRange.getStart()), paramType, intRange.getTarget()));
                }
                else {
                    providers.add(new IntProvider(param, paramType, intRange));
                }
                expression = clearTarget(expression, intRange.getTarget());
                continue;
            }

            // 查询浮点数表达式
            doubleRange = routeProcessor.getDoubleRange(expression);
            if (null != doubleRange) {
                if (doubleRange.isConstant()) {
                    providers.add(new ConstantReplaceProvider(String.valueOf(doubleRange.getStart()), paramType, doubleRange.getTarget()));
                }
                else {
                    providers.add(new DoubleProvider(param, paramType, doubleRange));
                }
                expression = clearTarget(expression, doubleRange.getTarget());
                continue;
            }

            break;
        }

        int size = providers.size();
        if (size == 0) {
            return new ConstantProvider(param, paramType);
        }

        if (size == 1) {
            return providers.get(0);
        }

        return new ComplexProvider(param, paramType, providers.toArray(new ArgsProvider[size]));
    }

    private String clearTarget(String source, String target) {
        return StringUtils.replaceOnce(source, target, "");
    }
}
