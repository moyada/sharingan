package cn.moyada.dubbo.faker.core.model;

import org.openjdk.jol.info.ClassLayout;

import java.sql.Timestamp;

/**
 * @author xueyikang
 * @create 2017-12-30 18:14
 */
public class InvokeFuture {

    public static void main(String[] args) {
        System.out.println(ClassLayout.parseClass(InvokeFuture.class).toPrintable());
        System.out.println(ClassLayout.parseClass(FutureResult.class).toPrintable());
    }

    private FutureResult future;

    private Timestamp invokeTime;

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
