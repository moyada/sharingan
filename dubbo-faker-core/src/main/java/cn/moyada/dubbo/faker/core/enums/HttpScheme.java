package cn.moyada.dubbo.faker.core.enums;

/**
 * @author xueyikang
 * @create 2018-01-07 12:35
 */
public enum HttpScheme {

    HTTP("http"), HTTPS("https");

    private String scheme;

    HttpScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getScheme() {
        return scheme;
    }
}
