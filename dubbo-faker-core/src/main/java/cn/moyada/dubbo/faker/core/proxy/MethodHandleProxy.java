package cn.moyada.dubbo.faker.core.proxy;

import cn.moyada.dubbo.faker.core.handle.AbstractHandle;
import cn.moyada.dubbo.faker.core.handle.MethodInvokeHandle;
import cn.moyada.dubbo.faker.core.utils.BeanUtil;
import cn.moyada.dubbo.faker.core.exception.NoSuchClassException;
import cn.moyada.dubbo.faker.core.model.FakerProxy;
import cn.moyada.dubbo.faker.core.model.MethodInvokeDO;
import cn.moyada.dubbo.faker.core.utils.ReflectUtil;
import com.alibaba.dubbo.rpc.RpcException;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.invoke.MethodHandle;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xueyikang
 * @create 2017-12-31 16:02
 */
public class MethodHandleProxy {

    private ClassPathXmlApplicationContext context;

    private final String xmlPath;

    private final AbstractHandle handle;

    private final Map<Integer, SoftReference<FakerProxy>> proxyMap;

    public MethodHandleProxy(String xmlPath) {
        this.handle = new MethodInvokeHandle();
        this.proxyMap = new HashMap<>();
        this.xmlPath = xmlPath;
    }

    public FakerProxy getProxy(MethodInvokeDO invokeInfo, int poolSize) {
        if(null == context) {
            context = new ClassPathXmlApplicationContext(new String[]{this.xmlPath});
        }

        FakerProxy proxy;

        // check if has cache proxy
        Integer id = invokeInfo.getId();
        SoftReference<FakerProxy> ref = proxyMap.get(id);
        if(null != ref) {
            proxy = ref.get();
            if (null != proxy && poolSize == proxy.getService().length) {
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
                throw new IllegalArgumentException("Failed to fetch parameter class: " + e);
            }
        }

        // fetch method handle
        MethodHandle methodHandle;
        try {
            methodHandle = handle.fetchHandleInfo(invokeInfo.getClassName(), invokeInfo.getMethodName(),
                    invokeInfo.getReturnType(), paramTypes);
        }
        catch (Exception e) {
            throw new NoSuchMethodError("Failed to fetch method error, class: " +
                    invokeInfo.getClassName() + ", method: " +
                    invokeInfo.getMethodName() + ", return: " +
                    invokeInfo.getReturnType() + ", param: " +
                    Arrays.toString(paramTypes) + ", " +
                    e);
        }

        if(null == methodHandle) {
            throw new NoSuchMethodError("Failed to fetch method error, class: " +
                    invokeInfo.getClassName() + ", method: " +
                    invokeInfo.getMethodName() + ", return: " +
                    invokeInfo.getReturnType() + ", param: " +
                    Arrays.toString(paramTypes));
        }

        // get service instance
        Class classType;
        try {
            classType = ReflectUtil.getClassType(invokeInfo.getClassName());
        } catch (ClassNotFoundException e) {
            throw new NoSuchClassException("Failed to fetch class error: ", e);
        }

        Object[] service;
        try {
            service = new Object[poolSize];
            for (int index = 0; index < poolSize; index++ ) {
                service[index] = BeanUtil.getBean(context, classType);
            }
        }
        catch (BeansException e) {
            throw new RpcException("Failed to invoke the method subscribe in the service " + invokeInfo.getClassName() + ".", e);
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
