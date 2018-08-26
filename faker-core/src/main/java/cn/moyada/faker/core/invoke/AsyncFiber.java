package cn.moyada.faker.core.invoke;

import co.paralleluniverse.fibers.FiberAsync;

/**
 * @author xueyikang
 * @create 2018-02-03 23:39
 */
public class AsyncFiber extends FiberAsync<Object, Exception> implements FiberCompletion {

    @Override
    public void success(Object result) {
        asyncCompleted(result);
    }

    @Override
    public void failure(Exception exception) {
        asyncFailed(exception);
    }

    @Override
    protected void requestAsync() {

    }
}
