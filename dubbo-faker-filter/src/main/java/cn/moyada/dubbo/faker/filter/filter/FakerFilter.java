package cn.moyada.dubbo.faker.filter.filter;

import cn.moyada.dubbo.faker.filter.listener.BatchRecordListener;
import cn.moyada.dubbo.faker.filter.utils.PropertyUtil;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务请求拦截器
 * @author xueyikang
 * @create 2018-03-29 18:44
 */
@Activate(group = Constants.PROVIDER, order = -999)
public class FakerFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(FakerFilter.class);

    private final boolean exception;
    private final boolean nullable;

    private BatchRecordListener listener;

    public FakerFilter() {
        log.info("Initializing FakerFilter.");
        setListener();
        exception = PropertyUtil.getProperty("faker.exception.filter", true);
        nullable = PropertyUtil.getProperty("faker.nullable", false);
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
        BatchRecordListener listener = ServiceBean.getSpringContext().getBean(BatchRecordListener.class);
        if(null == listener) {
            throw new NullPointerException("can not find any listener bean instant.");
        }
        this.listener = listener;
    }
}
