package cn.xueyikang.dubbo.faker.core.proxy;

import cn.xueyikang.dubbo.faker.core.handle.AbstractHandle;
import cn.xueyikang.dubbo.faker.core.handle.MethodInvokeHandle;
import cn.xueyikang.dubbo.faker.core.model.FakerProxy;
import cn.xueyikang.dubbo.faker.core.model.MethodInvokeDO;
import cn.xueyikang.dubbo.faker.core.utils.BeanUtil;
import cn.xueyikang.dubbo.faker.core.utils.ReflectUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.invoke.MethodHandle;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xueyikang
 * @create 2017-12-31 16:02
 */
public class MethodHandleProxy {

    private final AbstractHandle handle;

    private final Map<Integer, SoftReference<FakerProxy>> proxyMap;

    public MethodHandleProxy() {
        this.handle = new MethodInvokeHandle();
        proxyMap = new HashMap<>();
    }

    public FakerProxy getProxy(ClassPathXmlApplicationContext context, MethodInvokeDO invokeInfo, int poolSize) {
        FakerProxy proxy;

        // check if has cache proxy
        Integer id = invokeInfo.getId();
        SoftReference<FakerProxy> ref = proxyMap.get(id);
        if(null != ref) {
            proxy = ref.get();
            if (null != proxy) {
                return proxy;
            }
        }

        // fetch param type
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

        // fetch method handle
        MethodHandle methodHandle;
        try {
            methodHandle = handle.fetchHandleInfo(invokeInfo.getClassName(), invokeInfo.getMethodName(),
                    invokeInfo.getReturnType(), paramTypes);
        }
        catch (Exception e) {
            throw new NoSuchMethodError("方法句柄获取失败" + e);
        }
        if(null == methodHandle) {
            throw new NoSuchMethodError("方法句柄获取失败");
        }

        // get service instance
        Class classType;
        try {
            classType = ReflectUtil.getClassType(invokeInfo.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        Object[] service;
        try {
            service = new Object[poolSize];
            for (int index = 0; index < poolSize; index++ ) {
                service[index] = BeanUtil.getBean(context, classType);
            }
        }
        catch (BeansException e) {
            e.printStackTrace();
            return null;
        }
        proxy = new FakerProxy();
        proxy.setParamTypes(paramTypes);
        proxy.setMethodHandle(methodHandle);
        proxy.setService(service);

        // put ref proxy into cache map
        ref = new SoftReference<>(proxy);
        proxyMap.put(id, ref);
        return proxy;
    }
}
