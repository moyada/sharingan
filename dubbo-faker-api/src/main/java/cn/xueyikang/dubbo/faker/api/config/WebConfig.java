package cn.xueyikang.dubbo.faker.api.config;

import cn.xueyikang.dubbo.faker.api.interceptor.FakerInterceptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer, InitializingBean {

    private String applicationName;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FakerInterceptor(this.applicationName));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(null == applicationName) {
            throw new RuntimeException("FakerInterceptor Init Error: applicationName can not be null.");
        }
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}