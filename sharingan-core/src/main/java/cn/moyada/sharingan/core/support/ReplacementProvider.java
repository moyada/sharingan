package cn.moyada.sharingan.core.support;

import cn.moyada.sharingan.common.exception.InitializeInvokerException;
import cn.moyada.sharingan.storage.api.ArgsRepository;

import java.util.List;

public class ReplacementProvider extends AbstractProvider implements ArgsProvider {

    private static final int DEFAULT_THRESHOLD = 1000;

    private final ArgsRepository argsRepository;

    private final int threshold;
    private final IndexProvider indexProvider;

    private List<String> paramData;

    private final String target;
    private final int appId;
    private final String paramDomain;

    private final int total;
    private int time;

    public ReplacementProvider(String value, Class<?> paramType,
                               RouteInfo routeInfo, ArgsRepository argsRepository, boolean isRandom) {
        super(value, paramType);

        this.target = routeInfo.getTarget();
        this.appId = routeInfo.getAppId();
        this.paramDomain = routeInfo.getDomain();

        this.argsRepository = argsRepository;

        int total = argsRepository.countInvocationArgs(appId, paramDomain);
        if (total == 0) {
            throw new InitializeInvokerException("invocation args is empty. appId: " + appId + ", paramDomain: " + paramDomain);
        }
        this.total = total;
        this.time = 0;

        this.threshold = total > DEFAULT_THRESHOLD ? DEFAULT_THRESHOLD : total;
        this.indexProvider = isRandom ? new RandomIndexProvider(threshold) : new OrderIndexProvider(threshold);
    }

    @Override
    public Object fetchNext() {
        if (time > threshold || null == paramData) {
            this.paramData = argsRepository.findRandomInvocationArgs(appId, paramDomain, total, threshold);
        }
        int index = indexProvider.nextIndex();
        String data = paramData.get(index);

        String newValue = value.replace(target, data);
        Object next = convert(newValue, paramType);
        time++;
        return next;
    }
}
