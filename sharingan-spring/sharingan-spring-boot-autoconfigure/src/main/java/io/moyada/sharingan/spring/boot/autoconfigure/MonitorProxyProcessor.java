package io.moyada.sharingan.spring.boot.autoconfigure;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MonitorProxyProcessor extends AbstractAdvisorAutoProxyCreator {

    @Override
    protected List<Advisor> findCandidateAdvisors() {
        return super.findCandidateAdvisors();
    }
}
