package cn.moyada.sharingan.common.exception;

public abstract class SupperException extends RuntimeException {

    protected SupperException(String message) {
        super(message, null, false, false);
    }
}
