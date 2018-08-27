package cn.moyada.faker.core.handler;

import cn.moyada.faker.manager.domain.LogDO;
import cn.moyada.faker.rpc.api.invoke.Result;

/**
 * @author xueyikang
 * @create 2018-08-27 14:54
 */
public interface RecordHandler {

    LogDO receive(Result result);
}
