package io.moyada.sharingan.infrastructure.invoke;

/**
 * 请求数据
 * @author xueyikang
 * @since 0.0.1
 */
public class Invocation {

    /**
     * 调用参数
     */
    private Object[] argsValue;

    public Invocation(Object[] argsValue) {
        this.argsValue = argsValue;
    }

    public Object[] getArgsValue() {
        return argsValue;
    }
}
