package io.moyada.sharingan.expression.provider;


import io.moyada.sharingan.domain.expression.DataRepository;
import io.moyada.sharingan.domain.metadada.AppData;
import io.moyada.sharingan.domain.metadada.MetadataRepository;
import io.moyada.sharingan.infrastructure.exception.InitializeInvokerException;

import java.util.List;
import java.util.function.Supplier;

/**
 * 表达式替换提供器
 */
public class ResourceReplaceProvider extends ReplacementProvider implements ArgsProvider {

    private static final int DEFAULT_THRESHOLD = 1000;

    private Supplier<List<String>> dataSupplier;

    // 备选数据
    private List<String> data;

    // 下标提供器
    private final IndexSupplier indexSupplier;

    // 数据总数
    private int total;

    // 数据刷新值
    private int threshold;

    // 次数
    private int time;

    public ResourceReplaceProvider(String value, Class<?> paramType, String target, boolean isRandom,
                                   MetadataRepository metadataRepository, DataRepository dataRepository,
                                   String appName, String domain) {
        super(value, paramType, target);
        this.indexSupplier = isRandom ? new RandomIndexSupplier() : new OrderIndexSupplier();
        setResource(metadataRepository, dataRepository, appName, domain);
    }

    private void setResource(MetadataRepository metadataRepository, DataRepository dataRepository, String appName, String domain) {
        AppData appData = metadataRepository.findAppByName(appName);
        if (null == appData) {
            throw new InitializeInvokerException("unable to fetch application info, name = " + appName);
        }
        int total = dataRepository.count(appData.getId(), domain);
        if (total == 0) {
            throw new InitializeInvokerException("args resource is empty. appName: " + appName + ", domain: " + domain);
        }
        this.total = total;
        this.dataSupplier = () -> dataRepository.findRandomArgs(appData.getId(), domain, total, DEFAULT_THRESHOLD);
        freshData();
    }

    private void freshData() {
        if (time < threshold) {
            return;
        }

        synchronized (this) {
            if (time < threshold) {
                return;
            }

            this.data = dataSupplier.get();
            if (data.isEmpty()) {
                this.data = dataSupplier.get();
                if (data.isEmpty()) {
                    throw new InitializeInvokerException("invocation args is empty.");
                }
            }
            this.time = 0;
            this.threshold = (int) ((total > DEFAULT_THRESHOLD ? DEFAULT_THRESHOLD : total) * 0.7);
            this.indexSupplier.setTotal(data.size());
        }
    }

    @Override
    protected String next() {
        freshData();
        time++;
        int index = indexSupplier.nextIndex();
        return data.get(index);
    }
}
