package cn.moyada.dubbo.faker.core.model;

/**
 * @author xueyikang
 * @create 2018-02-03 22:48
 */
public class FutureResult<T> {

    private boolean success;

    private T result;

    private long spend;

    public static <T> FutureResult success(T data) {
        return new FutureResult<>(data, true);
    }

    public static <T> FutureResult success(T data, long spend) {
        return new FutureResult<>(data, spend, false);
    }

    public static <T> FutureResult failed(T data) {
        return new FutureResult<>(data, false);
    }

    public static <T> FutureResult failed(T data, long spend) {
        return new FutureResult<>(data, spend, false);
    }

    private FutureResult(T result, long spend, boolean success) {
        this.result = result;
        this.spend = spend;
        this.success = success;
    }

    private FutureResult(T result, boolean success) {
        this.result = result;
        this.success = success;
    }

    public void setSpend(long spend) {
        this.spend = spend;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getResult() {
        return result;
    }

    public long getSpend() {
        return spend;
    }
}
