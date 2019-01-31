package io.moyada.sharingan.infrastructure.exception;

/**
 * 初始化调用异常
 * @author xueyikang
 * @create 2018-03-26 23:04
 */
public class InitializeInvokerException extends SupperException {

    private static final long serialVersionUID = 5870600724106568612L;

    public InitializeInvokerException(Throwable cause) {
        super(cause);
    }

    public InitializeInvokerException(String message) {
        super(message);
    }
}
