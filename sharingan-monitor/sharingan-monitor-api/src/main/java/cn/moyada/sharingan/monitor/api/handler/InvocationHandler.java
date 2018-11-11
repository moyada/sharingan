package cn.moyada.sharingan.monitor.api.handler;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface InvocationHandler<T> {

    void handle(T t);
}
