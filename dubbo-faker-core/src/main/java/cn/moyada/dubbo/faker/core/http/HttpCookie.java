package cn.moyada.dubbo.faker.core.http;

/**
 * @author xueyikang
 * @create 2018-01-07 12:06
 */
public class HttpCookie {

    private String domain;

    private String path;

    private String name;

    private String value;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
