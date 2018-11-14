package cn.moyada.sharingan.core.support;

import cn.moyada.sharingan.common.util.AssertUtil;
import cn.moyada.sharingan.storage.api.MetadataRepository;
import cn.moyada.sharingan.storage.api.domain.AppDO;
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
     * @param value
     * @return
     */
    public RouteInfo getRoute(String value) {
        String expression = RegexUtil.findExpression(value);
        if (null == expression) {
            return null;
        }

        String[] route = RegexUtil.findRoute(expression);
        AssertUtil.checkoutNotNull(route, "cannot find any route from " + expression);

        String appName = route[0];
        AppDO appDO = metadataRepository.findAppByName(appName);
        AssertUtil.checkoutNotNull(appDO, "cannot find app info by " + appName);
        Integer appId = appDO.getId();

        String domain = route[1];
        AssertUtil.checkoutNotNull(domain, "args domain is not exist in " + expression);

        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setTarget(expression);
        routeInfo.setAppId(appId);
        routeInfo.setDomain(route[1]);
        return routeInfo;
    }

    public IntRange getIntRange(String value) {
        String expression = RegexUtil.findInt(value);
        if (null == expression) {
            return null;
        }

        IntRange intRange = RegexUtil.findIntRange(expression);
        intRange.setTarget(expression);
        return intRange;
    }

    public DoubleRange getDoubleRange(String value) {
        String expression = RegexUtil.findDouble(value);
        if (null == expression) {
            return null;
        }
        return RegexUtil.findDoubleRange(expression);
    }
}
