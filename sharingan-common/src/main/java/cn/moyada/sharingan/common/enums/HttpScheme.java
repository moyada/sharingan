package cn.moyada.sharingan.common.enums;

/**
 * @author xueyikang
 * @create 2018-01-07 12:35
 */
public enum HttpScheme {

    HTTP("http://"), HTTPS("https://");

    private String scheme;

    HttpScheme(String scheme) {
        this.scheme = scheme;
    }

    public static boolean checkout(String url) {
        for (HttpScheme httpScheme : HttpScheme.values()) {
            if (url.startsWith(httpScheme.scheme)) {
                return true;
            }
        }
        return false;
    }
}
