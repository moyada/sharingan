package io.moyada.sharingan.spring.boot.autoconfigure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class ProcessorSupport {

    protected final Log log;

    public ProcessorSupport() {
        this.log = LogFactory.getLog(this.getClass());
    }
}
