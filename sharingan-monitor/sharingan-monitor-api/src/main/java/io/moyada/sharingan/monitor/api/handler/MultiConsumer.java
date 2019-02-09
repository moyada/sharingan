package io.moyada.sharingan.monitor.api.handler;

import java.util.Collection;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface MultiConsumer<E> {

    void consumer(Collection<E> data);
}
