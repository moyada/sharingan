package cn.moyada.sharingan.monitor.api.entity;

/**
 * 标准持久化数据
 * @author xueyikang
 * @since 1.0
 **/
public class DefaultRecord implements Record<String> {

    private String application;

    private String domain;

    private String protocol;

    private String args;

    @Override
    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }
}
