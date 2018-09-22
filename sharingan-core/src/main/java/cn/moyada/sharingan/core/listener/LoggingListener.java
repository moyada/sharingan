package cn.moyada.sharingan.core.listener;


import cn.moyada.sharingan.core.handler.InvokeRecordHandler;
import cn.moyada.sharingan.rpc.api.invoke.InvokeReceiver;
import cn.moyada.sharingan.rpc.api.invoke.Result;
import cn.moyada.sharingan.storage.api.domain.InvocationResultDO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;


/**
 * 记录结果监听器
 * @author xueyikang
 * @create 2018-03-18 17:32
 */
public class LoggingListener extends AbstractListener implements InvokeReceiver {
    private static final Logger log = LogManager.getLogger(LoggingListener.class);

    public LoggingListener(InvokeRecordHandler recordHandler, Queue<Result> queue, int totalCount) {
        super(recordHandler, queue, totalCount);
    }

    @Override
    public void execution() {
        while (true) {
            InvocationResultDO resultDO = pickUp();
            if (null == resultDO) {
                continue;
            }

            invocationRepository.saveResult(resultDO);
            if (release(1)) {
                break;
            }
        }

        notifyWait();
    }
}
