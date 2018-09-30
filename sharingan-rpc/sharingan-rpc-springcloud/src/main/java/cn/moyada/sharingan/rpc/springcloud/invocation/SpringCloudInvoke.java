package cn.moyada.sharingan.rpc.springcloud.invocation;

import cn.moyada.sharingan.common.exception.InstanceNotFountException;
import cn.moyada.sharingan.common.task.DestroyTask;
import cn.moyada.sharingan.common.utils.RegexUtil;
import cn.moyada.sharingan.common.utils.StringUtil;
import cn.moyada.sharingan.common.utils.TimeUtil;
import cn.moyada.sharingan.rpc.api.invoke.*;
import feign.Client;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.Decoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Component("springcloudInvoke")
public class SpringCloudInvoke extends AsyncMethodInvoke implements ApplicationContextAware, AsyncInvoke, InvokeProxy {

    @Autowired
    private DestroyTask destroyTask;

    @Value("${eureka.client.serviceUrl.defaultZone}")
    private String registerUrl;

//    private Feign.Builder builder;

    private FeignContext feignContext;
    private Client client;
    private Decoder decoder;
    private Request.Options options;

    private RequestTemplate baseTemplate;

    private String[] params;
    private int paramSize;

    @PostConstruct
    public void initConfig() {
        if (StringUtil.isEmpty(registerUrl)) {
            destroyTask.addDestroyBean("springcloudInvoke");
            return;
        }
    }

    @Override
    public void initialize(InvocationMetaDate metaDate) throws InstanceNotFountException {
        if (null == client) {
            this.client = getInstance(LoadBalancerFeignClient.class);
        }
        if (null == decoder) {
            this.decoder = getInstance(Decoder.class);
        }
        if (null == options) {
            this.options = getInstance(Request.Options.class);
        }

        this.baseTemplate = create(metaDate);
        InvocationMetaDate.HttpInfo httpInfo = metaDate.getHttpInfo();

        List<String> paramList = RegexUtil.findPathVariable(metaDate.getMethodName());
        this.paramSize = paramList.size();

        String[] paramsName = httpInfo.getParam();
        if (null != paramsName && paramsName.length > 0) {
            this.paramSize += paramsName.length;
            paramList.addAll(Arrays.asList(paramsName));
        }

        String[] headers = httpInfo.getHeader();
        if (null != headers && headers.length > 0) {
            this.paramSize += headers.length;
            paramList.addAll(Arrays.asList(headers));
        }

        this.params = paramList.toArray(new String[0]);

        beforeInvoke();
    }

    private void beforeInvoke() {
        Object[] args = new Object[paramSize];
        for (int index = 0; index < paramSize; index++) {
            args[index] = "";
        }
        Invocation invocation = new Invocation();
        invocation.setArgsValue(args);
        execute(invocation);
    }

    private static RequestTemplate create(InvocationMetaDate metaDate) {
        InvocationMetaDate.HttpInfo httpInfo = metaDate.getHttpInfo();

        RequestTemplate template = new RequestTemplate();
        template.append(metaDate.getMethodName());
        template.method(httpInfo.getHttpType());
        template.insert(0, "http://" + metaDate.getApplicationName().toUpperCase());

        String[] param = httpInfo.getParam();
        if (null != param) {
            for (String s : param) {
                template.query(s, "{" + s + "}");
            }
        }
        String[] header = httpInfo.getHeader();
        if (null != header) {
            for (String s : header) {
                template.query(s, "{" + s + "}");
            }
        }
        return template;
    }

    @Override
    public Result execute(Invocation invocation) {
        RequestTemplate requestTemplate = new RequestTemplate(baseTemplate);
        resolveArgs(requestTemplate, invocation.getArgsValue());

        Result result;
        long begin = TimeUtil.currentTimeMillis();
        try {
            Request request = requestTemplate.request();
            Response response = client.execute(request, options);
            result = Result.success(decoder.decode(response, String.class));
        } catch (IOException e) {
            result = Result.failed(e.getMessage());
        } catch (Exception e) {
            result = Result.failed(e.getMessage());
        }

        result.setStartTime(new Timestamp(begin));
        // 完成计算耗时
        result.setResponseTime((int) (TimeUtil.currentTimeMillis() - begin));

        return result;
    }

    /**
     * 解析参数
     * @param requestTemplate
     * @param argsValue
     */
    private void resolveArgs(RequestTemplate requestTemplate, Object[] argsValue) {
        if (paramSize == 0) {
            return;
        }
        Map<String, Object> paramMap = new HashMap<>();
        Object value;
        for (int index = 0; index < paramSize; index++) {
            value = argsValue[index];
            if (null != value) {
                paramMap.put(params[index], value);
            }
        }
        requestTemplate.resolve(paramMap);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        FeignContext feignContext = applicationContext.getBean(FeignContext.class);
        if (null == feignContext) {
            return;
        }
        this.feignContext = feignContext;
    }

    private <T> T getInstance(Class<T> clazz) {
        return feignContext.getInstance(clazz.getName(), clazz);
    }
}
