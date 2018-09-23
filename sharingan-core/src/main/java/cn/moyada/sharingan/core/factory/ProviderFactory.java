package cn.moyada.sharingan.core.factory;

import cn.moyada.sharingan.core.provider.*;
import cn.moyada.sharingan.core.support.*;
import cn.moyada.sharingan.storage.api.ArgsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        String param;
        RouteInfo route;
        IntRange intRange;
        DoubleRange doubleRange;

        for (int index = 0; index < length; index++) {
            param = params[index];
            // 查询替换表达式
            route = routeProcessor.getRoute(param);
            if (null != route) {
                providers[index] = new ReplacementProvider(param, paramTypes[index], route, argsRepository, isRandom);
                continue;
            }

            // 查询整数表达式
            intRange = routeProcessor.getIntRange(param);
            if (null != intRange) {
                if (intRange.isConstant()) {
                    providers[index] = new ConstantProvider(String.valueOf(intRange.getStart()), paramTypes[index]);
                }
                else {
                    providers[index] = new IntProvider(param, paramTypes[index], intRange);
                }
                continue;
            }

            // 查询浮点数表达式
            doubleRange = routeProcessor.getDoubleRange(param);
            if (null != doubleRange) {
                if (doubleRange.isConstant()) {
                    providers[index] = new ConstantProvider(String.valueOf(doubleRange.getStart()), paramTypes[index]);
                }
                else {
                    providers[index] = new DoubleProvider(param, paramTypes[index], doubleRange);
                }
                continue;
            }

            providers[index] = new ConstantProvider(param, paramTypes[index]);
        }

        ArgsProviderContainer container = ArgsProviderContainer.emptyContainer();
        container.setProviders(providers);
        return container;
    }
}
