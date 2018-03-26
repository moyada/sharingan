package cn.moyada.dubbo.faker.core.proxy;

import cn.moyada.dubbo.faker.core.exception.InitializeInvokerException;
import cn.moyada.dubbo.faker.core.handle.AbstractHandle;
import cn.moyada.dubbo.faker.core.handle.MethodInvokeHandle;
import cn.moyada.dubbo.faker.core.model.FakerProxy;
import cn.moyada.dubbo.faker.core.model.MethodInvokeDO;
import cn.moyada.dubbo.faker.core.utils.BeanUtil;
import cn.moyada.dubbo.faker.core.utils.ReflectUtil;
import com.alibaba.dubbo.rpc.RpcException;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.invoke.MethodHandle;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用代理生成器
 * @author xueyikang
 * @create 2017-12-31 16:02
 */
public class MethodHandleProxy {

    private final ClassPathXmlApplicationContext context;

    private final AbstractHandle handle;

    private final Map<Integer, SoftReference<FakerProxy>> proxyMap;

    public MethodHandleProxy(String xmlPath) {
        this.handle = new MethodInvokeHandle();
        this.proxyMap = new HashMap<>();
        this.context = new ClassPathXmlApplicationContext(new String[]{xmlPath});
    }

    public FakerProxy getProxy(MethodInvokeDO invokeInfo) {
        FakerProxy proxy;

        // 检测是否已存在
        Integer id = invokeInfo.getId();
        SoftReference<FakerProxy> ref = proxyMap.get(id);
        if(null != ref) {
            proxy = ref.get();
            if (null != proxy) {
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
        Object service;
        try {
            service = BeanUtil.getBean(context, classType);
        }
        catch (BeansException e) {
            throw new RpcException("获取接口实例失败: " + invokeInfo.getClassName() + ".", e);
        }

        proxy = new FakerProxy();
        proxy.setParamTypes(paramTypes);
        proxy.setMethodHandle(methodHandle);
        proxy.setService(service);

        // 缓存调用代理
        ref = new SoftReference<>(proxy);
        proxyMap.put(id, ref);
        return proxy;
    }
}
