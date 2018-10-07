package cn.moyada.sharingan.rpc.dubbo;

import cn.moyada.sharingan.rpc.api.invoke.InvokeProxy;
import cn.moyada.sharingan.rpc.dubbo.config.DubboConfig;
import cn.moyada.sharingan.rpc.dubbo.invocation.DubboInvoke;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
@Configuration
@ConditionalOnClass(DubboConfig.class)
@ConditionalOnProperty(value = "dubbo.registry", matchIfMissing = true)
public class DubboAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = DubboConfig.class, search = SearchStrategy.CURRENT)
    public DubboConfig dubboConfigBean() {
        return new DubboConfig();
    }

    @Bean("dubboInvoke")
    @ConditionalOnMissingBean(DubboInvoke.class)
    public InvokeProxy dubboInvoke() {
        return new DubboInvoke();
    }
}
