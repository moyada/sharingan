package cn.moyada.dubbo.faker.filter.exception;

/**
 * 启动异常
 * @author xueyikang
 * @create 2018-03-30 01:50
 */
public class FakerInitException extends RuntimeException {

    private static final long serialVersionUID = 6939878587237922371L;

    public static final FakerInitException appNameNotFound = new FakerInitException("找不到对应的faker.appName值.");
    public static final FakerInitException groupIdNotFound = new FakerInitException("找不到对应的faker.groupId值.");
    public static final FakerInitException artifactIdNotFound = new FakerInitException("找不到对应的faker.artifactId值.");

    public FakerInitException(String message) {
        super(message);
    }
}
