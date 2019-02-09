package io.moyada.sharingan.monitor.api.exception;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MonitorException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public MonitorException(String message) {
        super(message);
    }

    public MonitorException(String message, Throwable cause) {
        super(message, cause);
    }

    public MonitorException(Throwable cause) {
        super(cause);
    }
}
