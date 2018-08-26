package cn.moyada.faker.rpc.api.invoke;

public class Result<T> {

    private boolean success;

    private String args;

    private T result;

    private String exception;

    private long responseTime;

    public static <T> Result success(T data) {
        return new Result<>(data);
    }

    public static <T> Result failed(String msg) {
        return new Result<>(msg);
    }

    public Result(T result) {
        this.result = result;
        this.success = true;
    }

    public Result(String msg) {
        this.exception = msg;
        this.success = false;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getResult() {
        return result;
    }

    public String getException() {
        return exception;
    }

    public long getResponseTime() {
        return responseTime;
    }
}
