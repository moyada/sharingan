package cn.moyada.sharingan.core.invoke;

/**
 * @author xueyikang
 * @create 2018-02-03 23:38
 */
public interface FiberCompletion {

    void success(Object result);

    void failure(Exception exception);
}
