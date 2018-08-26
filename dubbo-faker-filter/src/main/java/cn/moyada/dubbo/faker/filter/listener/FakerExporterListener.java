package cn.moyada.dubbo.faker.filter.listener;

import cn.moyada.dubbo.faker.filter.common.Context;
import cn.moyada.dubbo.faker.filter.domain.AppInfoDO;
import cn.moyada.dubbo.faker.filter.domain.MethodInvokeDO;
import cn.moyada.dubbo.faker.filter.exception.FakerInitException;
import cn.moyada.dubbo.faker.filter.manager.FakerManager;
import cn.moyada.dubbo.faker.filter.utils.PropertyUtil;
import cn.moyada.dubbo.faker.filter.utils.ReflectUtil;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.listener.ExporterListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 监听服务暴露
 * @author xueyikang
 * @create 2018-03-29 19:10
 */
@Activate(group = Constants.PROVIDER, order = -999)
public class FakerExporterListener extends ExporterListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(FakerExporterListener.class);

    private FakerManager fakerManager;

    public FakerExporterListener() {
        log.info("Initializing FakerExporterListener.");
    }

    @Override
    public void exported(Exporter<?> exporter) throws RpcException {
        if(null == fakerManager) {
            setFakerManager();
        }
        // 获取项目信息保存上下文
        AppInfoDO appInfoDO = getAppInfo();
        Integer appId = fakerManager.getAppId(appInfoDO);
        Context.setAppInfo(appId, appInfoDO.getAppName());

        Class<?> anInterface = exporter.getInvoker().getInterface();
        Method[] methods = anInterface.getMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(cn.moyada.dubbo.faker.api.annotation.Exporter.class)) {
                joinInvoker(anInterface, method);
            }
        }
    }

    /**
     * 获取项目信息
     * @return
     */
    private AppInfoDO getAppInfo() {
        String appName = PropertyUtil.getPropertyOnFile("faker.appName", "faker.properties");
        if(null == appName) {
            throw FakerInitException.appNameNotFound;
        }
        String groupId = PropertyUtil.getPropertyOnFile("faker.groupId", "faker.properties");
        if(null == groupId) {
            throw FakerInitException.groupIdNotFound;
        }
        String artifactId = PropertyUtil.getPropertyOnFile("faker.artifactId", "faker.properties");
        if(null == artifactId) {
            throw FakerInitException.artifactIdNotFound;
        }
        String version = PropertyUtil.getPropertyOnFile("faker.version", "faker.properties");
        String url = PropertyUtil.getPropertyOnFile("faker.url", "faker.properties");
        AppInfoDO appInfoDO = new AppInfoDO();
        appInfoDO.setAppName(appName);
        appInfoDO.setGroupId(groupId);
        appInfoDO.setArtifactId(artifactId);
        appInfoDO.setVersion(version);
        appInfoDO.setUrl(url);
        return appInfoDO;
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
        methodInvokeDO.setOverride(annotation.override());
        return methodInvokeDO;
    }

    public void setFakerManager() {
        FakerManager fakerManager = ServiceBean.getSpringContext().getBean(FakerManager.class);
        if(null == fakerManager) {
            throw new NullPointerException("can not find fakerManager bean instant.");
        }
        this.fakerManager = fakerManager;
    }
}