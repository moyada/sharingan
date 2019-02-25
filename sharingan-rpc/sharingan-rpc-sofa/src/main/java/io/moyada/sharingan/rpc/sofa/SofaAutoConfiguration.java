package io.moyada.sharingan.rpc.sofa;


import io.moyada.sharingan.infrastructure.invoke.InvokeProxy;
import io.moyada.sharingan.rpc.sofa.config.SofaConfig;
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
@ConditionalOnClass(SofaConfig.class)
@ConditionalOnProperty(value = SofaAutoConfiguration.REGISTER_URL)
public class SofaAutoConfiguration implements EnvironmentAware {

    final static String REGISTER_URL = "sharingan.rpc.sofa.registry";

    public static final String BEAN_NAME = "sofaInvoke";

    private Environment env;

    @Bean
    @ConditionalOnMissingBean(value = SofaConfig.class, search = SearchStrategy.CURRENT)
    public SofaConfig sofaConfig() {
        SofaConfig sofaConfig = new SofaConfig();
        sofaConfig.setRegistry(env.getProperty(SofaConfig.PREFIX + ".registry"));
        sofaConfig.setProtocol(env.getProperty(SofaConfig.PREFIX + ".protocol"));
        sofaConfig.setDirectUrl(env.getProperty(SofaConfig.PREFIX + ".direct-url"));
        sofaConfig.setTimeout(env.getProperty(SofaConfig.PREFIX + ".timeout", Integer.class));
        return sofaConfig;
    }

    @Bean(SofaAutoConfiguration.BEAN_NAME)
    @ConditionalOnMissingBean(SofaInvoke.class)
    public InvokeProxy sofaInvoke() {
        return new SofaInvoke();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}
