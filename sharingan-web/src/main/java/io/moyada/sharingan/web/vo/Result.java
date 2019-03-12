package io.moyada.sharingan.web.vo;

import java.io.Serializable;

/**
 * @author xueyikang
 * @create 2018-01-01 15:53
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 5280258032251442037L;

    private Integer code;

    private T data;

    private String msg;

    private boolean success;

    public static <T> Result<T> success(T data) {
        Result r = new Result();
        r.code = 200;
        r.data = data;
        r.success = true;
        return r;
    }

    public static Result failed(Integer code, String msg) {
        Result r = new Result();
        r.code = code;
        r.msg = msg;
        r.success = false;
        return r;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
