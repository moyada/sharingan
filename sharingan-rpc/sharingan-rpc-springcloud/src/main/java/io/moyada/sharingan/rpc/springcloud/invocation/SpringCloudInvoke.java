package io.moyada.sharingan.rpc.springcloud.invocation;


import feign.Client;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.Decoder;
import io.moyada.sharingan.infrastructure.ContextFactory;
import io.moyada.sharingan.infrastructure.constant.HttpStatus;
import io.moyada.sharingan.infrastructure.exception.InstanceNotFountException;
import io.moyada.sharingan.infrastructure.invoke.AsyncMethodInvoke;
import io.moyada.sharingan.infrastructure.invoke.Invocation;
import io.moyada.sharingan.infrastructure.invoke.data.HttpInvocation;
import io.moyada.sharingan.infrastructure.invoke.data.Result;
import io.moyada.sharingan.infrastructure.util.StringUtil;
import io.moyada.sharingan.rpc.springcloud.EurekaAutoConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * SpringCloud-Eureka调用器
 * @author xueyikang
 * @since 0.0.1
 **/
public class SpringCloudInvoke extends AsyncMethodInvoke<Request, HttpInvocation> implements ApplicationContextAware {

    @Autowired
    private ContextFactory contextFactory;

    @Value("${" + EurekaAutoConfiguration.REGISTER_URL + "}")
    private String registerUrl;

    @Autowired
    private EurekaClientConfigBean configBean;

    private FeignContext feignContext;
    private Client client;
    private Decoder decoder;
    private Request.Options options;

    private RequestTemplate baseTemplate;

    private String[] params;
    private int paramSize;
    private boolean hasBody;
    private boolean isEmpty;

    @PostConstruct
    public void initConfig() {
        if (StringUtil.isEmpty(registerUrl)) {
            // 无效注册地址，关闭注册，销毁实例
            configBean.setFetchRegistry(false);
            contextFactory.destroyBean(EurekaAutoConfiguration.BEAN_NAME);
            return;
        }
    }

    @Override
    protected void doInitialize(HttpInvocation metaDate) throws InstanceNotFountException {
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

        this.hasBody = metaDate.isHasBody();

//        List<String> paramList = RegexUtil.findPathVariable(metaDate.getMethodName());
        List<String> paramList = new ArrayList<>();

        String[] paramsName = metaDate.getParam();
        if (null != paramsName && paramsName.length > 0) {
            paramList.addAll(Arrays.asList(paramsName));
        }

        String[] headers = metaDate.getHeader();
        if (null != headers && headers.length > 0) {
            paramList.addAll(Arrays.asList(headers));
        }

        this.params = paramList.toArray(new String[0]);
        this.paramSize = params.length;

        this.isEmpty = paramSize == 0 && !hasBody;
    }

    /**
     * 预处理请求
     */
    @Override
    protected void beforeInvoke() {
        int size = hasBody ? paramSize + 1 : paramSize;
        Object[] args = new Object[size];
        for (int index = 0; index < size; index++) {
            args[index] = "";
        }
        Invocation invocation = new Invocation(args);
        Result result = execute(invocation);
        if (!result.isSuccess()) {
            String exception = result.getException();
            if (exception.contains("does not have available server")) {
                throw new InstanceNotFountException(exception);
            }
        }
    }

    /**
     * 创建请求模版
     * @param metaDate
     * @return
     */
    private static RequestTemplate create(HttpInvocation metaDate) {
        RequestTemplate template = new RequestTemplate();
        template.append(metaDate.getMethodName());
        template.method(metaDate.getHttpType());
        template.insert(0, "http://" + metaDate.getApplicationName().toUpperCase());

        String[] param = metaDate.getParam();
        if (null != param) {
            for (String s : param) {
                template.query(s, "{" + s + "}");
            }
        }
        String[] header = metaDate.getHeader();
        if (null != header) {
            for (String s : header) {
                template.query(s, "{" + s + "}");
            }
        }

        if (null != metaDate.getContentType()) {
            template.header("Content-Type", metaDate.getContentType());
        }
        return template;
    }

    @Override
    protected Request resolve(Invocation invocation) {
        RequestTemplate requestTemplate = new RequestTemplate(baseTemplate);
        resolveArgs(requestTemplate, invocation.getArgsValue());
        return requestTemplate.request();
    }

    @Override
    public Result invoke(Request request) {
        Result result;
        try {
            Response response = client.execute(request, options);
            if (response.status() == HttpStatus.OK) {
                result = Result.success(decoder.decode(response, String.class));
            } else {
                result = Result.failed(decoder.decode(response, String.class).toString());
            }
        } catch (IOException e) {
            result = Result.failed(e.getMessage());
        } catch (Exception e) {
            result = Result.failed(e.getMessage());
        }

        return result;
    }

    /**
     * 解析参数
     * @param requestTemplate
     * @param argsValue
     */
    private void resolveArgs(RequestTemplate requestTemplate, Object[] argsValue) {
        if (isEmpty) {
            return;
        }

        if (hasBody) {
            try {
                requestTemplate.body(argsValue[0].toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> paramMap = new HashMap<>();
        Object value;
        for (int index = hasBody ? 1 : 0; index < paramSize; index++) {
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
