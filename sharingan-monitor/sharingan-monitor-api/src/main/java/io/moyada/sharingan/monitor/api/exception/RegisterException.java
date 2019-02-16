package io.moyada.sharingan.monitor.api.exception;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class RegisterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegisterException(Throwable cause) {
        super(cause);
    }

    public RegisterException(String message) {
        super(message);
    }

    public RegisterException(String message, Throwable cause) {
        super(message, cause);
    }
}
