package cn.moyada.sharingan.core.listener;

import cn.moyada.sharingan.core.handler.InvokeRecordHandler;
import cn.moyada.sharingan.rpc.api.invoke.InvokeCallback;
import cn.moyada.sharingan.rpc.api.invoke.Result;
import cn.moyada.sharingan.storage.api.domain.InvocationResultDO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * 批量保存结果监听器
 * @author xueyikang
 * @create 2018-03-18 17:32
 */
public class BatchLoggingListener extends AbstractListener implements InvokeCallback {
    private static final Logger log = LogManager.getLogger(BatchLoggingListener.class);

    private static final int OPT_SIZE = 1000;

    public BatchLoggingListener(InvokeRecordHandler recordHandler, Queue<Result> queue, int totalCount) {
        super(recordHandler, queue, totalCount);
    }

    @Override
    public void execution() {
        List<InvocationResultDO> results = new ArrayList<>(OPT_SIZE);
        int size = 0;
        while (true) {
            InvocationResultDO resultDO = pickUp();
            if (null != resultDO) {
                size++;
                results.add(resultDO);
                if (size != OPT_SIZE) {
                    continue;
                }
            } else if (size == 0) {
                continue;
            }

            invocationRepository.saveResult(results);
            results.clear();
            if (release(size)) {
                break;
            }
            size = 0;
        }

        notifyWait();
    }
}
