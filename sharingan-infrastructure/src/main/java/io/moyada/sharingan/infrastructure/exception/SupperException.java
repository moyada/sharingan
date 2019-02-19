package io.moyada.sharingan.infrastructure.exception;

public abstract class SupperException extends RuntimeException {

    public SupperException(Throwable cause) {
        super(cause.getMessage(), cause, false, false);
    }

    protected SupperException(String message) {
        super(message, null, false, false);
    }
}
