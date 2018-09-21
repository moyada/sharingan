package cn.moyada.sharingan.rpc.api.invoke;

import java.sql.Timestamp;

public class Result<T> {

    private boolean success;

    private String arguments;

    private T result;

    private String exception;

    private Timestamp startTime;

    private int responseTime;

    public static <T> Result success(T data) {
        return new Result<>(data);
    }

    public static Result failed(String msg) {
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
