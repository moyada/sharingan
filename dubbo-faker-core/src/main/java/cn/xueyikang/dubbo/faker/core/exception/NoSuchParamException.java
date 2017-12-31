package cn.xueyikang.dubbo.faker.core.exception;

/**
 * @author xueyikang
 * @create 2017-12-31 13:21
 */
public class NoSuchParamException extends RuntimeException {

    private static final long serialVersionUID = 2877923473711743460L;

    public NoSuchParamException(String message) {
        super(message);
    }
}
