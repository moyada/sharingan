package cn.moyada.dubbo.faker.core.request;

import cn.moyada.dubbo.faker.core.enums.ConvertType;
import cn.moyada.dubbo.faker.core.invoke.AbstractInvoke;
import cn.moyada.dubbo.faker.core.invoke.FiberInvoke;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.*;
import cn.moyada.dubbo.faker.core.proxy.MethodHandleProxy;
import cn.moyada.dubbo.faker.core.thread.LoggingListener;
import cn.moyada.dubbo.faker.core.utils.ConvertUtil;
import cn.moyada.dubbo.faker.core.utils.JsonUtil;
import cn.moyada.dubbo.faker.core.utils.ParamUtil;
import cn.moyada.dubbo.faker.core.utils.UUIDUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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
        if(null == invokeInfo) {
            return "Failed to fetch invoke info, checkout method_invoke table.";
        }

        FakerProxy proxy = methodHandleProxy.getProxy(invokeInfo, poolSize);
        if(null == proxy) {
            return "Failed to fetch service proxy.";
        }

        Object[] values = JsonUtil.toArray(invokeExpression, Object.class);
        Class<?>[] paramTypes = proxy.getParamTypes();
        if(null == values || paramTypes.length != values.length) {
            return "Error input param or don't match invoke param.";
        }

        RebuildParam rebuildParam = ParamUtil.getRebuildParam(values);

        Map<Integer, Map<String, String>> rebuildParamMap = rebuildParam.getRebuildParamMap();
        Map<String, List<String>> fakerParamMap = fakerManager.getFakerParamMapByRebuildParam(rebuildParam.getRebuildParamSet());

        // find param convert
        Map<Integer, ConvertType> convertMap = ConvertUtil.getConvertMap(paramTypes);

        // generator fakerId
        String fakerId = UUIDUtil.getUUID();

        MethodHandle methodHandle = proxy.getMethodHandle();
        Object[] service = proxy.getService();

        // init invoke thread pool
        Queue<InvokeFuture> queue = new ConcurrentLinkedQueue<>();
        AbstractInvoke invoke = new FiberInvoke(poolSize, queue);

        // init logging thread poll
        LoggingListener loggingListener = 1 == questNum ?
                new LoggingListener(1) : new LoggingListener(questNum / 20);
        loggingListener.run(fakerId, invokeId, queue, fakerManager, saveResult, resultParam);

        log.info("start faker invoke: " + fakerId);

        // convert param and invoke method
        int timeout = 1 <= qps ? 100 : 3600 / qps;
        if(timeout > 50) {
            for (int index = 0; index < questNum; index++) {

                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Object[] argsValue = ParamUtil.convertValue(values, paramTypes, rebuildParamMap, fakerParamMap, convertMap);

                invoke.invoke(methodHandle, service[index % poolSize], argsValue, Arrays.toString(argsValue));
            }
        }
        else {
            for (int index = 0; index < questNum; index++) {
                // random invoke param
                //values = fakerParam.get(index).split(",");
                Object[] argsValue = ParamUtil.convertValue(values, paramTypes, rebuildParamMap, fakerParamMap, convertMap);

                invoke.invoke(methodHandle, service[0], argsValue, Arrays.toString(argsValue));
            }
        }

        invoke.destroy();

        log.info("faker invoke done: " + fakerId);

        // destroy thread pool
        while (!queue.isEmpty()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        loggingListener.shutdown();

        log.info("shutdown");

        return "fakerId：" + fakerId;
    }

    public static void main(String[] args) {
        Object[] objects = JsonUtil.toArray("[\"${1.brand}\", {\"displayType\": \"INNER\"}]", Object.class);

        ParamDO paramDO = new ParamDO();
        paramDO.setId(123L);
        paramDO.setMethodId(666);
        paramDO.setParamValue("test");
        String json = JsonUtil.toJson(paramDO);
        System.out.println(JsonUtil.toJson(new String[]{"123", "123"}));
        System.out.println(json);
        paramDO = JsonUtil.toObject(json, ParamDO.class);

        int[] ints = JsonUtil.toObject("[1,2,3,4,5]", int[].class);

        String invokeParam = "[\"${123.model}\", {\"action\":\"haha\",\"money\":1111}, \"wishenm\"]";
        Object[] array1 = JsonUtil.toArray(invokeParam, Object.class);
        System.out.println(array1[1] instanceof Map);
        System.out.println(array1);


        boolean array = String[].class.isArray();
        String[] strs = new String[]{"666", "xxx"};
        String str = JsonUtil.toJson(strs);
        String[] strings = JsonUtil.toObject(str, String[].class);
        List<MethodInvokeDO> list = Lists.newArrayList();
        MethodInvokeDO invokeDO = new MethodInvokeDO();
        invokeDO.setAppId(12);
        invokeDO.setAppName("rewre");
        invokeDO.setClassName("com.cdaf.cdsf.d.fsd");
        invokeDO.setMethodName("dsfsfdsfdsf");
        invokeDO.setParamType("ddfdf,dfd,sf,dsf,dsf");
        invokeDO.setReturnType("ddfdf,dfd,sf,dsf,dsf");
        list.add(invokeDO);
        str = JsonUtil.toJson(list);
        List<Object> list1 = JsonUtil.toList(str, Object.class);
        System.out.println(list.getClass().getSimpleName());
        System.out.println(List.class.getSimpleName());
    }
}
