package cn.xueyikang.dubbo.faker.core.request;

import cn.xueyikang.dubbo.faker.core.handle.AbstractHandle;
import cn.xueyikang.dubbo.faker.core.handle.MethodInvokeHandle;
import cn.xueyikang.dubbo.faker.core.invoke.AbstractInvoke;
import cn.xueyikang.dubbo.faker.core.invoke.AsyncInvoke;
import cn.xueyikang.dubbo.faker.core.manager.FakerManager;
import cn.xueyikang.dubbo.faker.core.model.MethodInvokeDO;
import cn.xueyikang.dubbo.faker.core.parser.JacksonParser;
import cn.xueyikang.dubbo.faker.core.parser.JsonParser;
import cn.xueyikang.dubbo.faker.core.utils.*;
import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandle;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class FakerRequest {

    private ClassPathXmlApplicationContext context;

    @Autowired
    private FakerManager fakerManager;

    private final AbstractHandle handle;

    private AbstractInvoke invoke;

    private final JsonParser parser;

    public FakerRequest() {
        this.handle = new MethodInvokeHandle();
        this.parser = new JacksonParser();
    }

    public String request(int appId, int invokeId, String type, Integer poolSize, Integer qps, Integer questNum) {
        if(null == context) {
            context = new ClassPathXmlApplicationContext(new String[]{"classpath:application-dubbo.xml"});
        }

        MethodInvokeDO invokeInfo = fakerManager.getInvokeInfo(invokeId);
        if(null == invokeInfo) {
            return "该请求调用不存在";
        }

        // get param type
        Class<?>[] paramTypes;
        String[] argsType = invokeInfo.getParamType().split(",");
        int length = argsType.length;
        if(0 == length) {
            paramTypes = new Class[0];
        }
        else {
            paramTypes = new Class[length];
            try {
                for (int index = 0; index < length; index++) {
                    paramTypes[index] = ReflectUtil.getClassType(argsType[index]);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Take Parameter Class Error: " + e);
            }
        }

        // get method handle
        MethodHandle methodHandle;
        try {
            methodHandle = handle.fetchHandleInfo(invokeInfo.getClassName(), invokeInfo.getMethodName(),
                    invokeInfo.getReturnType(), paramTypes);
        }
        catch (Exception e) {
            return "方法句柄获取失败" + e;
        }

        // get service instance
//        Class classType;
//        try {
//            classType = ReflectUtil.getClassType(invokeInfo.getClassName());
//        } catch (ClassNotFoundException e) {
//            return "依赖类未找到" + e;
//        }
        Object service;
        try {
            service = BeanUtil.getBean(context, invokeInfo.getClassName());
        }
        catch (BeansException e) {
            return "依赖实例未找到" + e;
        }

        List<String> fakerParam = fakerManager.getFakerParam(appId, type);


        // init invoke thread pool
        invoke = new AsyncInvoke(null == poolSize ? 10 : poolSize, fakerManager);
        int timeout = null == qps ? 100 : 3600 / qps;

        // find param convert
        Map<Integer, Integer> convertMap = ConvertUtil.getConvertMap(paramTypes);

        int size = null == questNum ? 100 : questNum, i, top = fakerParam.size();
        String[] values;
        // generator fakerId
        String fakerId = UUIDUtil.getUUID();
        Random random = new Random();
        // convert param and invoke method
        for (int index = 0; index < size; index++) {
            values = JsonUtil.toArray(fakerParam.get(random.nextInt(top)), String.class);
            //values = fakerParam.get(index).split(",");
            if(null == values) {
                continue;
            }
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            length = values.length;
            Object[] argsValue;

            if(0 == length) {
                argsValue = new Object[]{service};
            }
            else {
                argsValue = new Object[length + 1];
                argsValue[0] = service;
                for (i = 0; i < length; i++) {
                    if(1 == convertMap.get(i)) {
                        argsValue[i + 1] = JsonUtil.toList(values[i], Object.class);
                    }
                    else {
                        argsValue[i + 1] = JsonUtil.toObject(values[i], paramTypes[i]);
                    }
                }
            }
            invoke.invoke(fakerId, methodHandle, argsValue);
        }
        invoke.destroy();
        return "请求编号：" + fakerId;
    }

    public static void main(String[] args) {
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
