package cn.xueyikang.dubbo.faker.core.request;

import cn.xueyikang.dubbo.faker.core.invoke.AbstractInvoke;
import cn.xueyikang.dubbo.faker.core.invoke.AsyncInvoke;
import cn.xueyikang.dubbo.faker.core.manager.FakerManager;
import cn.xueyikang.dubbo.faker.core.model.FakerProxy;
import cn.xueyikang.dubbo.faker.core.model.InvokeFuture;
import cn.xueyikang.dubbo.faker.core.model.MethodInvokeDO;
import cn.xueyikang.dubbo.faker.core.model.RebuildParam;
import cn.xueyikang.dubbo.faker.core.proxy.MethodHandleProxy;
import cn.xueyikang.dubbo.faker.core.thread.LoggingListener;
import cn.xueyikang.dubbo.faker.core.utils.ConvertUtil;
import cn.xueyikang.dubbo.faker.core.utils.JsonUtil;
import cn.xueyikang.dubbo.faker.core.utils.ParamUtil;
import cn.xueyikang.dubbo.faker.core.utils.UUIDUtil;
import com.google.common.collect.Lists;
import com.souche.car.model.common.create.ColorDTO;
import com.souche.car.model.common.enums.ColorType;
import com.souche.car.model.common.enums.StatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandle;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class FakerRequest {
    private static final Logger log = LoggerFactory.getLogger(FakerRequest.class);

    private ClassPathXmlApplicationContext context;

    @Autowired
    private FakerManager fakerManager;

    private final MethodHandleProxy methodHandleProxy;

    public FakerRequest() {
        this.methodHandleProxy = new MethodHandleProxy();
    }

    public String request(int invokeId, String invokeExpression, int poolSize, int qps, int questNum,
                          boolean saveResult, String resultParam) {
        if(null == context) {
            context = new ClassPathXmlApplicationContext(new String[]{"classpath:application-dubbo.xml"});
        }

        MethodInvokeDO invokeInfo = fakerManager.getInvokeInfo(invokeId);
        if(null == invokeInfo) {
            return "Failed to fetch invoke info, checkout method_invoke table.";
        }

        FakerProxy proxy = methodHandleProxy.getProxy(context, invokeInfo, poolSize);
        if(null == proxy) {
            return "Failed to fetch service proxy.";
        }

        Object[] values = JsonUtil.toArray(invokeExpression, Object.class);

        Class<?>[] paramTypes = proxy.getParamTypes();
        if(null == values || paramTypes.length != values.length) {
            return "Error input param or don't match invoke param.";
        }

        RebuildParam rebuildParam = ParamUtil.getRebuildParam(values);

        Map<Integer, List<String>> rebuildParamMap = rebuildParam.getRebuildParamMap();
        Map<String, List<String>> paramMap = fakerManager.getFakerParamMapByRebuildParam(rebuildParam.getRebuildParamSet());

        // find param convert
        Map<Integer, Integer> convertMap = ConvertUtil.getConvertMap(paramTypes);

        // generator fakerId
        String fakerId = UUIDUtil.getUUID();

        MethodHandle methodHandle = proxy.getMethodHandle();
        Object[] service = proxy.getService();

        int timeout = 1 <= qps ? 100 : 3600 / qps;

        // init invoke thread pool
        AbstractInvoke invoke = new AsyncInvoke(poolSize);
        Queue<InvokeFuture> queue = new ConcurrentLinkedQueue<>();

        // async result
        CompletableFuture<Object> future;
        Instant start;

        // init logging thread poll
        LoggingListener loggingListener;
        if(1 == questNum) {

            loggingListener = new LoggingListener(1, 20);
            loggingListener.run(fakerId, invokeId, queue, fakerManager, saveResult, resultParam);
        }
        else {

            loggingListener = new LoggingListener(questNum / 15, 40);
            loggingListener.run(fakerId, invokeId, queue, fakerManager, saveResult, resultParam);
        }

        log.info("start faker invoke: " + fakerId);

        // convert param and invoke method
        for (int index = 0; index < questNum; index++) {
            // random invoke param
            //values = fakerParam.get(index).split(",");

            if(timeout > 50) {
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Object[] argsValue = ParamUtil.convertValue(values, paramTypes, rebuildParamMap, paramMap, convertMap);

            start = Instant.now();
            future = invoke.invoke(fakerId, methodHandle, service[questNum % poolSize], argsValue);

            queue.add(new InvokeFuture(start, future, JsonUtil.toJson(argsValue)));
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
        ColorDTO modelColorDTO = new ColorDTO();
        modelColorDTO.setColorName("黑");
        modelColorDTO.setColorType(ColorType.EXTERIOR_COLOR);
        modelColorDTO.setColorValue("#123412");
        modelColorDTO.setComment("haha");
        modelColorDTO.setStatusType(StatusType.ON);
        String json = JsonUtil.toJson(modelColorDTO);
        System.out.println(json);
        modelColorDTO = JsonUtil.toObject(json, ColorDTO.class);

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
