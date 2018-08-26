package cn.moyada.dubbo.faker.filter.listener;


import cn.moyada.dubbo.faker.api.annotation.Fetch;
import cn.moyada.dubbo.faker.filter.common.Context;
import cn.moyada.dubbo.faker.filter.log.LoggerBuilder;
import cn.moyada.dubbo.faker.filter.utils.PropertyUtil;
import com.alibaba.dubbo.rpc.Invocation;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 日志参数监听器
 * @author xueyikang
 * @create 2018-03-30 02:44
 */
public class LogRecordListener extends AbstractRecordListener {

//    private final int capacity;


    private final ExecutorService excutor;

    public LogRecordListener() {

        // 参数临时保存空间
//        capacity = PropertyUtil.getProperty("faker.capacity", 500);
        // 最大线程数
        int maxThread = PropertyUtil.getProperty("faker.maxThread", 10);
        // 间隔毫秒数
//        long interval = PropertyUtil.getProperty("faker.interval", 1000L);

        this.excutor = new ThreadPoolExecutor(1, maxThread, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public void saveRequest(Class<?> invokerInterface, Invocation invocation) {
        excutor.submit(new Recorder(invokerInterface, invocation));
    }

    /**
     * 记录参数
     */
    class Recorder implements Runnable {

        private Class<?> invokerInterface;
        private Invocation invocation;

        Recorder(Class<?> invokerInterface, Invocation invocation) {
            this.invokerInterface = invokerInterface;
            this.invocation = invocation;
        }

        @Override
        public void run() {
            Method method = fetchMethod(invokerInterface, invocation);
            if (null == method) {
                return;
            }
            String type = method.getAnnotation(Fetch.class).value();
            String args = getArgs(invocation.getArguments());
            if(null == args) {
                return;
            }
            LoggerBuilder.getLogger(Context.getAppInfo().getAppName() + "-" + type).info(args);
        }
    }
}
