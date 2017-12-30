package cn.xueyikang.dubbo.faker.core.request;

import cn.xueyikang.dubbo.faker.core.handle.AbstractHandle;
import cn.xueyikang.dubbo.faker.core.handle.MethodInvokeHandle;
import cn.xueyikang.dubbo.faker.core.invoke.AbstractInvoke;
import cn.xueyikang.dubbo.faker.core.invoke.AsyncInvoke;
import cn.xueyikang.dubbo.faker.core.manager.FakerManager;
import cn.xueyikang.dubbo.faker.core.model.MethodInvokeDO;
import cn.xueyikang.dubbo.faker.core.parser.JacksonParser;
import cn.xueyikang.dubbo.faker.core.parser.JsonParser;
import cn.xueyikang.dubbo.faker.core.utils.BeanUtil;
import cn.xueyikang.dubbo.faker.core.utils.ReflectUtil;
import cn.xueyikang.dubbo.faker.core.utils.UUIDUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandle;
import java.util.List;

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

    public String request(int appId, int invokeId, String type) {
        if(null == context) {
            context = new ClassPathXmlApplicationContext(new String[]{"classpath:application-dubbo.xml"});
        }

        MethodInvokeDO invokeInfo = fakerManager.getInvokeInfo(invokeId);
        if(null == invokeInfo) {
            return "该请求调用不存在";
        }

        MethodHandle methodHandle;
        try {
            methodHandle = handle.fetchHandleInfo(invokeInfo.getClassName(), invokeInfo.getMethodName(),
                    invokeInfo.getReturnType(), invokeInfo.getParamType().split(","));
        }
        catch (Exception e) {
            return "方法句柄获取失败" + e;
        }

        Class classType;
        try {
            classType = ReflectUtil.getClassType(invokeInfo.getClassName());
        } catch (ClassNotFoundException e) {
            return "依赖类未找到" + e;
        }

        Object service;
        try {
            service = BeanUtil.getBean(context, classType);
        }
        catch (BeansException e) {
            return "依赖实例未找到" + e;
        }

        List<String> fakerParam = fakerManager.getFakerParam(appId, type);

        invoke = new AsyncInvoke(10, fakerManager);
        int size = fakerParam.size();
        Object[] values;
        int length;
        String fakerId = UUIDUtil.getUUID();
        for (int index = 0; index < size; index++) {
//          values = parser.toArray(fakerParam.get(index), Object.class);
            values = fakerParam.get(index).split(",");

            length = values.length;
            Object[] argsValue;

            if(0 == length) {
                argsValue = new Object[]{service};
            }
            else {
                argsValue = new Object[length + 1];
                argsValue[0] = service;
                System.arraycopy(values, 0, argsValue, 1, length);
            }
            invoke.invoke(fakerId, methodHandle, argsValue);
        }
        invoke.destroy();
        return "请求编号：" + fakerId;
    }
}
