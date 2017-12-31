package cn.xueyikang.dubbo.faker.core.exception;

/**
 * @author xueyikang
 * @create 2017-12-31 13:21
 */
public class NoSuchClassException extends RuntimeException {

    private static final long serialVersionUID = -2257812333299774846L;

    public NoSuchClassException(String message) {
        super(message);
    }

    public NoSuchClassException(Exception e) {
        super(e);
    }

    public NoSuchClassException(String msg, Exception e) {
        super(msg, e);
    }
}
