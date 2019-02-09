package io.moyada.sharingan.expression;


import io.moyada.sharingan.domain.metadada.AppData;
import io.moyada.sharingan.domain.metadada.MetadataRepository;
import io.moyada.sharingan.infrastructure.util.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 路由解析器
 * @author xueyikang
 * @since 0.0.1
 */
@Component
public class RouteProcessor {

    @Autowired
    private MetadataRepository metadataRepository;

    /**
     * 获取参数表达式路由信息
     * @param expression
     * @return
     */
    public RouteInfo getRoute(String expression) {
        String[] route = ExpressionAnalyser.findRoute(expression);
        AssertUtil.checkoutNotNull(route, "cannot find any route from " + expression);

        String appName = route[0];
        AppData appData = metadataRepository.findAppByName(appName);
        AssertUtil.checkoutNotNull(appData, "cannot find app info by " + appName);
        Integer appId = appData.getId();

        String domain = route[1];
        AssertUtil.checkoutNotNull(domain, "args domain is not exist in " + expression);

        return new RouteInfo(appId, route[1]);
    }
}
