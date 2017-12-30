package cn.xueyikang.dubbo.faker.core.model;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/**
 * @author xueyikang
 * @create 2017-12-30 18:14
 */
public class InvokeFuture {

    private Instant start;

    private CompletableFuture<Object> future;

    private String realParam;

    public InvokeFuture(Instant start, CompletableFuture<Object> future, String realParam) {
        this.start = start;
        this.future = future;
        this.realParam = realParam;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public CompletableFuture<Object> getFuture() {
        return future;
    }

    public void setFuture(CompletableFuture<Object> future) {
        this.future = future;
    }

    public String getRealParam() {
        return realParam;
    }

    public void setRealParam(String realParam) {
        this.realParam = realParam;
    }
}
