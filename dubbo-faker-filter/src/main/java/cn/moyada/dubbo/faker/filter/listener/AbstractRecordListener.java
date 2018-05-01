package cn.moyada.dubbo.faker.filter.listener;

import cn.moyada.dubbo.faker.api.annotation.Fetch;
import cn.moyada.dubbo.faker.filter.utils.JsonUtil;
import com.alibaba.dubbo.rpc.Invocation;

import java.lang.reflect.Method;

/**
 * @author xueyikang
 * @create 2018-04-13 11:37
 */
public abstract class AbstractRecordListener {

    public abstract void saveRequest(Class<?> invokerInterface, Invocation invocation);

    /**
     * 检测是否需要进行参数保存
     * @param invokerInterface
     * @param invocation
     * @return
     */
    protected Method fetchMethod(Class<?> invokerInterface, Invocation invocation) {
        Method method;
        try {
            method = invokerInterface.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }

        if (!method.isAnnotationPresent(Fetch.class)) {
            return null;
        }
        return method;
    }

    /**
     * 转换参数
     * @param arguments
     * @return
     */
    protected String getArgs(Object[] arguments) {
        String args;
        if (null == arguments) {
            args = null;
        } else {
            if (arguments.length == 1) {
                args = JsonUtil.toJson(arguments[0]);
            } else {
                args = JsonUtil.toJson(arguments);
            }
        }
        return args;
    }
}
