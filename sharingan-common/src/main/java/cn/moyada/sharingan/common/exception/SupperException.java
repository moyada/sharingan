package cn.moyada.sharingan.common.exception;

public abstract class SupperException extends RuntimeException {

    public SupperException(Throwable cause) {
        super(cause);
    }

    protected SupperException(String message) {
        super(message, null, false, false);
    }
}
