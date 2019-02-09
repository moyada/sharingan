package io.moyada.sharingan.infrastructure.invoke.data;

import java.sql.Timestamp;

/**
 * 调用结果
 * @param <T>
 */
public class Result<T> {

    // 正常响应
    private boolean success;

    // 调用参数json串
    private String arguments;

    // 请求结果
    private T result;

    // 异常信息
    private String exception;

    // 请求时间
    private Timestamp startTime;

    // 响应时间
    private int responseTime;

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static Result<String> failed(String msg) {
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

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public int getResponseTime() {
        return responseTime;
    }
}
