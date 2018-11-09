package cn.moyada.sharingan.manager.cache;

import cn.moyada.sharingan.monitor.api.Catch;
import cn.moyada.sharingan.monitor.api.Listener;
import cn.moyada.sharingan.monitor.api.RpcProtocol;
import cn.moyada.sharingan.storage.api.domain.InvocationReportDO;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Listener(domain = "test", protocol = RpcProtocol.DUBBO)
public interface CacheService {

    @Catch
    InvocationReportDO getReport(String fakerId);
}
