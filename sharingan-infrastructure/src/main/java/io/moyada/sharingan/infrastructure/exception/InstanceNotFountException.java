package io.moyada.sharingan.infrastructure.exception;

public class InstanceNotFountException extends SupperException {
    private static final long serialVersionUID = 7592435866783131410L;

    public InstanceNotFountException(Throwable cause) {
        super(cause);
    }

    public InstanceNotFountException(String message) {
        super(message);
    }
}
