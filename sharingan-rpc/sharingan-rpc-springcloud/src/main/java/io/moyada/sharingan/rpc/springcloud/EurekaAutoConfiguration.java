package io.moyada.sharingan.rpc.springcloud;


import com.netflix.discovery.EurekaClient;
import io.moyada.sharingan.infrastructure.ContextFactory;
import io.moyada.sharingan.infrastructure.invoke.InvokeProxy;
import io.moyada.sharingan.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
@Configuration
@ConditionalOnClass(SpringCloudInvoke.class)
@EnableDiscoveryClient
@EnableFeignClients
public class EurekaAutoConfiguration implements EnvironmentAware {

    private final static String REGISTER_URL = "sharingan.rpc.springcloud.registry";

    private Environment environment;

    @Autowired
    private ContextFactory contextFactory;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = EurekaAutoConfiguration.REGISTER_URL)
    public BeanFactoryPostProcessor feignShutdownProcessor() {
        return new FeignBeanFactoryPostProcessor();
    }

    @Bean("springcloudInvoke")
    @ConditionalOnMissingBean(SpringCloudInvoke.class)
    public InvokeProxy springCloudInvoke(EurekaClient eurekaClient) {
        if (StringUtil.isEmpty(environment.getProperty(EurekaAutoConfiguration.REGISTER_URL))) {
            // 无效注册地址，关闭注册，销毁实例
            eurekaClient.shutdown();
            contextFactory.destroyBean(eurekaClient);
            return null;
        }
        return new SpringCloudInvoke();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
