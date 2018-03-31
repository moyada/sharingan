package cn.moyada.dubbo.faker.core.proxy;

import cn.moyada.dubbo.faker.core.common.BeanHolder;
import cn.moyada.dubbo.faker.core.exception.InitializeInvokerException;
import cn.moyada.dubbo.faker.core.handler.AbstractHandler;
import cn.moyada.dubbo.faker.core.model.domain.MethodInvokeDO;
import cn.moyada.dubbo.faker.core.model.MethodProxy;
import cn.moyada.dubbo.faker.core.utils.ReflectUtil;
import com.alibaba.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandle;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用代理生成器
 * @author xueyikang
 * @create 2017-12-31 16:02
 */
@Component
public class MethodHandleProxy {
    private static final Logger log = LoggerFactory.getLogger(MethodHandleProxy.class);

    @Autowired
    private AbstractHandler handle;

    private final BeanHolder beanHolder;

    private final Map<Integer, SoftReference<MethodProxy>> proxyMap;

    public MethodHandleProxy() {
        this.beanHolder = new BeanHolder("classpath:application-dubbo.xml");
        this.proxyMap = new HashMap<>();
    }

    public MethodProxy getProxy(MethodInvokeDO invokeInfo) { //, int poolSize) {
        MethodProxy proxy;

        log.info("init method proxy info.");
        // 检测是否已存在
        Integer id = invokeInfo.getId();
        SoftReference<MethodProxy> ref = proxyMap.get(id);
        if(null != ref) {
            proxy = ref.get();
            if (null != proxy) { // && proxy.getService().length == poolSize) {
                return proxy;
            }
        }

        // 获取参数类型
        Class<?>[] paramTypes;
        String[] argsType = invokeInfo.getParamType().split(",");
        int length = argsType.length;
        if(0 == length) {
            paramTypes = new Class[0];
        }
        else {
            paramTypes = new Class[length];
            for (int index = 0; index < length; index++) {
                try {
                    paramTypes[index] = ReflectUtil.getClassType(argsType[index]);
                } catch (ClassNotFoundException e) {
                    throw new InitializeInvokerException("获取参数类型失败: " + argsType[index]);
                }
            }
        }

        // 获取方法具柄
        MethodHandle methodHandle = handle.fetchHandleInfo(invokeInfo.getClassName(),
                invokeInfo.getMethodName(), invokeInfo.getReturnType(), paramTypes);

        // 获取接口
        Class classType;
        try {
            classType = ReflectUtil.getClassType(invokeInfo.getClassName());
        } catch (ClassNotFoundException e) {
            throw new InitializeInvokerException("获取结果失败: " + invokeInfo.getClassName());
        }

        // 获取接口实例
        Object serviceAssembly;
//        Object[] serviceAssembly = new Object[poolSize];
        try {
            serviceAssembly = beanHolder.getBean(classType);
//            for (int index = 0; index < poolSize; index++) {
//                serviceAssembly[index] = beanHelper.getBean(classType);
//            }
        }
        catch (BeansException e) {
            throw new RpcException("获取接口实例失败: " + invokeInfo.getClassName() + ".", e);
        }

        // 初始化服务注册
//        for (Object service : serviceAssembly) {
            try {
                methodHandle.invoke(serviceAssembly, null);
            } catch (Throwable throwable) {
            }
//        }

        proxy = new MethodProxy();
        proxy.setParamTypes(paramTypes);
        proxy.setMethodHandle(methodHandle);
        proxy.setService(serviceAssembly);

        // 缓存调用代理
        ref = new SoftReference<>(proxy);
        proxyMap.put(id, ref);
        return proxy;
    }
}
