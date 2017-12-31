package cn.xueyikang.dubbo.faker.core.exception;

/**
 * @author xueyikang
 * @create 2018-01-01 00:54
 */
public class NoSuchProviderException extends RuntimeException {

    private static final long serialVersionUID = 2486956322973174201L;

    public NoSuchProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
