package cn.xueyikang.dubbo.faker.api.filter;

import com.alibaba.dubbo.rpc.*;

import java.lang.reflect.Method;


public class FakerFilter implements Filter {


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Class<?> invokerInterface = invoker.getInterface();
        String methodName = invocation.getMethodName();
        Class<?>[] parameterTypes = invocation.getParameterTypes();

        Method method;
        try {
            method = invokerInterface.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            method = null;
            e.printStackTrace();
        }

        if(null != method) {
            // TODO: 2017/12/25 Record the invoke info
            Object[] arguments = invocation.getArguments();

        }

        return invoker.invoke(invocation);
    }
}
