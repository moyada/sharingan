package io.moyada.sharingan.monitor.api.test;

import io.moyada.sharingan.monitor.api.entity.Invocation;
import io.moyada.sharingan.monitor.api.handler.InvocationConverter;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class TestInvocationConverter implements InvocationConverter<String> {

    @Override
    public String convert(Invocation invocation) {
        return invocation.toString();
    }
}
