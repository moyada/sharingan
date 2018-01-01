package cn.xueyikang.dubbo.faker.ui.model;

import java.io.Serializable;

/**
 * @author xueyikang
 * @create 2018-01-01 15:53
 */
public class Result<T> implements Serializable {

    private Integer code;

    private T data;

    private String msg;

    public static <T> Result<T> success(T data) {
        Result r = new Result();
        r.code = 200;
        r.data = data;
        return r;
    }

    public static Result failed(Integer code, String msg) {
        Result r = new Result();
        r.code = code;
        r.msg = msg;
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
}
