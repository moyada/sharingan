package io.moyada.sharingan.domain.task;

import java.util.concurrent.Executor;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface TaskExecutor extends Executor {

    void shutdown();
}
