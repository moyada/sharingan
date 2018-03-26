package cn.moyada.dubbo.faker.core.request;

import cn.moyada.dubbo.faker.core.enums.ConvertType;
import cn.moyada.dubbo.faker.core.invoke.AbstractInvoker;
import cn.moyada.dubbo.faker.core.invoke.AsyncInvoker;
import cn.moyada.dubbo.faker.core.listener.CompletedListener;
import cn.moyada.dubbo.faker.core.listener.LoggingListener;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.FakerProxy;
import cn.moyada.dubbo.faker.core.model.MethodInvokeDO;
import cn.moyada.dubbo.faker.core.model.ParamProvider;
import cn.moyada.dubbo.faker.core.proxy.MethodHandleProxy;
import cn.moyada.dubbo.faker.core.utils.ConvertUtil;
import cn.moyada.dubbo.faker.core.utils.JsonUtil;
import cn.moyada.dubbo.faker.core.utils.ParamUtil;
import cn.moyada.dubbo.faker.core.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

@Component
public class FakerRequest {
    private static final Logger log = LoggerFactory.getLogger(FakerRequest.class);

    @Autowired
    private FakerManager fakerManager;

    private final MethodHandleProxy methodHandleProxy;

    public FakerRequest() {
        this.methodHandleProxy = new MethodHandleProxy("classpath:application-dubbo.xml");
    }

    public String request(int invokeId, String invokeExpression, int poolSize, int qps, int questNum,
                          boolean saveResult, String resultParam) {
        MethodInvokeDO invokeInfo = fakerManager.getInvokeInfo(invokeId);
        FakerProxy proxy = methodHandleProxy.getProxy(invokeInfo);


        Object[] values = JsonUtil.toArray(invokeExpression, Object[].class);
        Class<?>[] paramTypes = proxy.getParamTypes();
        if(null == values || paramTypes.length != values.length) {
            return "参数表达式参数与调用方法参数个数不符.";
        }

        ParamProvider paramProvider = ParamUtil.getRebuildParam(values);

        Map<Integer, Map<String, String>> rebuildParamMap = paramProvider.getRebuildParamMap();
        Map<String, List<String>> fakerParamMap = fakerManager.getFakerParamMapByRebuildParam(paramProvider.getRebuildParamSet());

        // find param convert
        Map<Integer, ConvertType> convertMap = ConvertUtil.getConvertMap(paramTypes);

        // 生成调用报告编码
        String fakerId = UUIDUtil.getUUID();

        // 创建调用结果监听器
        CompletedListener listener = new LoggingListener(fakerId, invokeId, fakerManager, saveResult, resultParam);

        // 创建方法调用器
        AbstractInvoker invoke = new AsyncInvoker(proxy.getMethodHandle(), proxy.getService(),
                listener, poolSize);

        log.info("start faker invoke: " + fakerId);

        // convert param and invoke method
        int timeout = 10 >= qps ? 100 : (3600 / qps) - 20;

        if(timeout > 50) {
            for (int index = 0; index < questNum; index++) {
                Object[] argsValue = ParamUtil.fetchParam(values, paramTypes, rebuildParamMap, fakerParamMap, convertMap);

                invoke.invoke(argsValue, Arrays.toString(argsValue));

                LockSupport.parkNanos(timeout * 1000);
            }
        }
        else {
            for (int index = 0; index < questNum; index++) {
                // random invoke param
                //values = fakerParam.get(index).split(",");
                Object[] argsValue = ParamUtil.fetchParam(values, paramTypes, rebuildParamMap, fakerParamMap, convertMap);

                invoke.invoke(argsValue, Arrays.toString(argsValue));
            }
        }

        invoke.destroy();

        log.info("faker invoke done: " + fakerId);

        listener.shutdownDelay();

        log.info("shutdown");

        return "报告编号：" + fakerId;
    }
}
