package io.moyada.sharingan.rpc.dubbo;


import io.moyada.sharingan.rpc.dubbo.config.DubboConfig;
import io.moyada.sharingan.infrastructure.invoke.InvokeProxy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
@Configuration
@ConditionalOnClass(DubboConfig.class)
@ConditionalOnProperty(value = DubboConfig.PREFIX + ".registry")
public class DubboAutoConfiguration implements EnvironmentAware {

    static final String BEAN_NAME = "dubboInvoke";

    private Environment env;

    @Bean
    @ConditionalOnMissingBean(value = DubboConfig.class, search = SearchStrategy.CURRENT)
    public DubboConfig dubboConfig() {
        DubboConfig dubboConfig = new DubboConfig();
        dubboConfig.setRegistry(env.getProperty(DubboConfig.PREFIX + ".registry"));
        dubboConfig.setProtocol(env.getProperty(DubboConfig.PREFIX + ".protocol"));
        dubboConfig.setUsername(env.getProperty(DubboConfig.PREFIX + ".username"));
        dubboConfig.setPassword(env.getProperty(DubboConfig.PREFIX + ".password"));
        dubboConfig.setTimeout(env.getProperty(DubboConfig.PREFIX + ".timeout", Integer.class));
        return dubboConfig;
    }

    @Bean(DubboAutoConfiguration.BEAN_NAME)
    @ConditionalOnMissingBean(DubboInvoke.class)
    public InvokeProxy dubboInvoke() {
        return new DubboInvoke();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}
