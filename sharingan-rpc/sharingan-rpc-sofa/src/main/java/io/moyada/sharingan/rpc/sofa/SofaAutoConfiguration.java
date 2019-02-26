package io.moyada.sharingan.rpc.sofa;


import io.moyada.sharingan.infrastructure.ContextFactory;
import io.moyada.sharingan.infrastructure.config.DefaultConfig;
import io.moyada.sharingan.infrastructure.invoke.InvokeProxy;
import io.moyada.sharingan.infrastructure.util.StringUtil;
import io.moyada.sharingan.rpc.sofa.config.SofaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
public class SofaAutoConfiguration implements EnvironmentAware {

    private Environment env;

    @Autowired
    private ContextFactory contextFactory;

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

    @Bean("sofaInvoke")
    @ConditionalOnMissingBean(SofaInvoke.class)
    public InvokeProxy sofaInvoke(SofaConfig sofaConfig, DefaultConfig defaultConfig) {
        if (null == sofaConfig.getRegistry() && StringUtil.isEmpty(sofaConfig.getDirectUrl())) {
            contextFactory.destroyBean(sofaConfig);
            return null;
        }
        return new SofaInvoke(sofaConfig, defaultConfig);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}
