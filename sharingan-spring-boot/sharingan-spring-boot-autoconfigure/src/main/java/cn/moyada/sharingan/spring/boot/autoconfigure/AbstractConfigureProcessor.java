package cn.moyada.sharingan.spring.boot.autoconfigure;

import cn.moyada.sharingan.spring.boot.autoconfigure.config.SharinganProperties;
import cn.moyada.sharingan.spring.boot.autoconfigure.util.PropertiesUtil;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class AbstractConfigureProcessor implements EnvironmentAware, PriorityOrdered {

    protected boolean enable = false;

    protected Map<String, Object> attach;

    @Override
    public void setEnvironment(Environment environment) {
        Boolean property = environment.getProperty(SharinganProperties.ENABLE, boolean.class);
        if (null != property) {
            this.enable = property;
        }

        attach = PropertiesUtil.getMap(environment, SharinganProperties.ATTACH);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
