package cn.moyada.dubbo.faker.api.filter;

import cn.moyada.dubbo.faker.api.listener.BatchRecordListener;
import cn.moyada.dubbo.faker.api.utils.PropertyUtil;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求拦截器
 */
public class FakerFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(FakerFilter.class);

    private final boolean exception;
    private final boolean nullable;

    private BatchRecordListener listener;

    public FakerFilter() {
        log.info("init FakerFilter.");
        setListener();
        String fakerNullable = PropertyUtil.getProperty("faker.nullable", "faker.properties");
        if(null == fakerNullable || !fakerNullable.equals("true")) {
            nullable = false;
        }
        else {
            nullable = true;
        }

        String successEnable = PropertyUtil.getProperty("faker.success.enable", "faker.properties");
        if(null == successEnable || !successEnable.equals("true")) {
            exception = false;
        }
        else {
            exception = true;
        }
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = invoker.invoke(invocation);

        // 结果不能为空
        if(null == result.getValue() && !nullable) {
            return result;
        }
        // 调用不能报错
        if(result.hasException() && !exception) {
            return result;
        }

        listener.saveRequest(invoker.getInterface(), invocation);
        return result;
    }

    public void setListener() {
        BatchRecordListener listener = ServiceBean.getSpringContext().getBean("batchRecordListener", BatchRecordListener.class);
        this.listener = listener;
    }
}
