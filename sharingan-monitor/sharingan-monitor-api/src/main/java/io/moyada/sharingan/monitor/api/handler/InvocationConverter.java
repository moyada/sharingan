package io.moyada.sharingan.monitor.api.handler;

import io.moyada.sharingan.monitor.api.entity.Invocation;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface InvocationConverter<E> {

    E convert(Invocation invocation);
}
