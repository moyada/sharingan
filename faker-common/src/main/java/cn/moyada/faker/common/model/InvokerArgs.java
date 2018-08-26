package cn.moyada.faker.common.model;

/**
 * @author xueyikang
 * @create 2018-04-04 12:32
 */
public class InvokerArgs {

    private Object[] argsValue;

    public InvokerArgs(Object[] argsValue) {
        this.argsValue = argsValue;
    }

    public Object[] getArgsValue() {
        return argsValue;
    }

    public void setArgsValue(Object[] argsValue) {
        this.argsValue = argsValue;
    }
}
