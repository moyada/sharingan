package cn.moyada.dubbo.faker.core.exception;

/**
 * 初始化调用异常
 * @author xueyikang
 * @create 2018-03-26 23:04
 */
public class InitializeInvokerException extends RuntimeException {

    private static final long serialVersionUID = 5870600724106568612L;

    public static final InitializeInvokerException methodError =
            new InitializeInvokerException("请求方法不存在.");

    public static final InitializeInvokerException paramError =
            new InitializeInvokerException("参数表达式参数与调用方法参数个数不符.");

    public InitializeInvokerException(String message) {
        super(message);
    }
}
