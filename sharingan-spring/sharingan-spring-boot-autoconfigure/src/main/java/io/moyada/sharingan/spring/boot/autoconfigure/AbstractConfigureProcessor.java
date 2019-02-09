package io.moyada.sharingan.spring.boot.autoconfigure;

import io.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class AbstractConfigureProcessor implements PriorityOrdered {

    protected SharinganConfig sharinganConfig;

    public AbstractConfigureProcessor(SharinganConfig sharinganConfig) {
        this.sharinganConfig = sharinganConfig;
    }

//    @Override
//    public void setEnvironment(Environment environment) {
//        this.attach = PropertiesUtil.getMap(environment, SharinganProperties.ATTACH);
//    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
