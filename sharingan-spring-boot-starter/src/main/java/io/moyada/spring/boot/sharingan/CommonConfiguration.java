package io.moyada.spring.boot.sharingan;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class CommonConfiguration {

    protected final Log log;

    public CommonConfiguration() {
        this.log = LogFactory.getLog(this.getClass());
    }
}
