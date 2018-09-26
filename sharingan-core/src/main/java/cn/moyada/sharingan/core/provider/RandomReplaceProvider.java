package cn.moyada.sharingan.core.provider;

import cn.moyada.sharingan.common.exception.InitializeInvokerException;
import cn.moyada.sharingan.core.support.RouteInfo;
import cn.moyada.sharingan.storage.api.ArgsRepository;

import java.util.List;

/**
 * 表达式替换提供器
 */
public class RandomReplaceProvider extends ReplacementProvider implements ArgsProvider {

    private static final int DEFAULT_THRESHOLD = 1000;

    // 参数数据
    private final ArgsRepository argsRepository;

    // 项目编号
    private final int appId;
    // 数据领域
    private final String paramDomain;

    // 备选数据
    private List<String> paramData;

    // 数据总数
    private final int total;

    // 数据刷新值
    private final int threshold;

    // 次数
    private int time;

    // 下标提供器
    private final IndexProvider indexProvider;

    public RandomReplaceProvider(String value, Class<?> paramType,
                                 RouteInfo routeInfo, ArgsRepository argsRepository, boolean isRandom) {
        super(value, paramType, routeInfo.getTarget());

        this.appId = routeInfo.getAppId();
        this.paramDomain = routeInfo.getDomain();

        this.argsRepository = argsRepository;

        int total = argsRepository.countInvocationArgs(appId, paramDomain);
        if (total == 0) {
            throw new InitializeInvokerException("invocation args is empty. appId: " + appId + ", paramDomain: " + paramDomain);
        }
        this.total = total;
        this.time = 0;
        this.threshold = (int) ((total > DEFAULT_THRESHOLD ? DEFAULT_THRESHOLD : total) * 0.7);

        this.indexProvider = isRandom ? new RandomIndexProvider(threshold) : new OrderIndexProvider(threshold);
    }

    @Override
    protected String next() {
        fresh();
        int index = indexProvider.nextIndex();
        String data = paramData.get(index);
        time++;
        return data;
    }

    private void fresh() {
        if (time < threshold && null != paramData) {
            return;
        }
        this.paramData = argsRepository.findRandomInvocationArgs(appId, paramDomain, total, threshold);
        this.time = 0;
    }
}
