package cn.moyada.dubbo.faker.api.listener;

import cn.moyada.dubbo.faker.api.common.Context;
import cn.moyada.dubbo.faker.api.domain.MethodInvokeDO;
import cn.moyada.dubbo.faker.api.exception.FakerInitException;
import cn.moyada.dubbo.faker.api.manager.FakerManager;
import cn.moyada.dubbo.faker.api.utils.PropertyUtil;
import cn.moyada.dubbo.faker.api.utils.ReflectUtil;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.ExporterListener;
import com.alibaba.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 暴露监听
 */
public class FakerExporterListener implements ExporterListener {
    private static final Logger log = LoggerFactory.getLogger(FakerExporterListener.class);

    private FakerManager fakerManager;

    @Override
    public void exported(Exporter<?> exporter) throws RpcException {
        if(null == fakerManager) {
            setFakerManager();
        }
        // 获取项目信息保存上下文
        String appName = getAppName();
        Integer appId = fakerManager.getAppId(appName);
        Context.setAppInfo(appId, appName);

        Class<?> anInterface = exporter.getInvoker().getInterface();
        Method[] methods = anInterface.getMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(cn.moyada.dubbo.faker.api.annotation.Exporter.class)) {
                joinInvoker(anInterface, method);
            }
        }
    }

    @Override
    public void unexported(Exporter<?> exporter) {

    }

    private String getAppName() {
        String appName = PropertyUtil.getPropertyOnFile("faker.appName", "faker.properties");
        if(null == appName) {
            throw FakerInitException.appNameNotFound;
        }
        return appName;
    }

    /**
     * 保存方法调用信息
     * @param aClass
     * @param method
     */
    private void joinInvoker(Class<?> aClass, Method method) {
        MethodInvokeDO methodInvokeDO = convert(aClass, method);
        fakerManager.addMethodInvoke(methodInvokeDO);
        log.info("exported " + methodInvokeDO.getClassName() + " " + methodInvokeDO.getMethodName() + " " + methodInvokeDO.getParamType());
    }

    private MethodInvokeDO convert(Class<?> aClass, Method method) {
        MethodInvokeDO methodInvokeDO = new MethodInvokeDO();

        Context.AppInfo appInfo = Context.getAppInfo();
        methodInvokeDO.setAppId(appInfo.getAppId());
        methodInvokeDO.setAppName(appInfo.getAppName());

        methodInvokeDO.setClassName(aClass.getName());
        methodInvokeDO.setMethodName(method.getName());
        methodInvokeDO.setParamType(ReflectUtil.getParameterTypeName(method.getParameterTypes()));
        methodInvokeDO.setReturnType(ReflectUtil.getReturnTypeName(method.getReturnType()));

        cn.moyada.dubbo.faker.api.annotation.Exporter annotation = method.getAnnotation(cn.moyada.dubbo.faker.api.annotation.Exporter.class);
        methodInvokeDO.setExpression(annotation.value());
        return methodInvokeDO;
    }

    public void setFakerManager() {
        FakerManager fakerManager = ServiceBean.getSpringContext().getBean("fakerManager", FakerManager.class);
        this.fakerManager = fakerManager;
    }
}