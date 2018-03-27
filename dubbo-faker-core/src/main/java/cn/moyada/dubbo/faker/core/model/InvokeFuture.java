package cn.moyada.dubbo.faker.core.model;

import java.sql.Timestamp;

/**
 * 调用结果
 * @author xueyikang
 * @create 2017-12-30 18:14
 */
public class InvokeFuture {

    /**
     * 请求结果
     */
    private FutureResult future;

    /**
     * 发起时间
     */
    private Timestamp invokeTime;

    /**
     * 请求参数
     */
    private String realParam;

    public InvokeFuture(FutureResult future, Timestamp invokeTime, String realParam) {
        this.future = future;
        this.invokeTime = invokeTime;
        this.realParam = realParam;
    }

    public FutureResult getFuture() {
        return future;
    }

    public String getRealParam() {
        return realParam;
    }

    public Timestamp getInvokeTime() {
        return invokeTime;
    }
}
