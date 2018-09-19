package cn.moyada.sharingan.core.factory;

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

        for (int index = 0; index < length; index++) {
            param = params[index];
            route = routeProcessor.getRoute(param);
            if (null == route) {
                providers[index] = new ConstantProvider(param, paramTypes[index]);
                continue;
            }

            providers[index] = new ReplacementProvider(param, paramTypes[index], route, argsRepository, isRandom);
        }

        ArgsProviderContainer container = ArgsProviderContainer.emptyContainer();
        container.setProviders(providers);
        return container;
    }
}
