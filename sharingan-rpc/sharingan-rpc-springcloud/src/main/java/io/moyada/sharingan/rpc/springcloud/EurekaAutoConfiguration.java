package io.moyada.sharingan.rpc.springcloud;


import io.moyada.sharingan.infrastructure.invoke.InvokeProxy;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
@Configuration
@ConditionalOnClass(SpringCloudInvoke.class)
@ConditionalOnProperty(value = EurekaAutoConfiguration.REGISTER_URL)
@EnableDiscoveryClient
@EnableFeignClients
public class EurekaAutoConfiguration {

    final static String REGISTER_URL = "sharingan.rpc.springcloud.registry";

    final static String BEAN_NAME = "springcloudInvoke";

    @Bean
    @ConditionalOnMissingBean
    public BeanFactoryPostProcessor feignShutdownProcessor() {
        return new FeignBeanFactoryPostProcessor();
    }

    @Bean(EurekaAutoConfiguration.BEAN_NAME)
    @ConditionalOnMissingBean(SpringCloudInvoke.class)
    public InvokeProxy springCloudInvoke() {
        return new SpringCloudInvoke();
    }
}
