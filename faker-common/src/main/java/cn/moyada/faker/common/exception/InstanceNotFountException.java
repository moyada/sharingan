package cn.moyada.faker.common.exception;

public class InstanceNotFountException extends RuntimeException {
    private static final long serialVersionUID = 7592435866783131410L;

    public InstanceNotFountException() {
    }

    public InstanceNotFountException(String message) {
        super(message);
    }
}
