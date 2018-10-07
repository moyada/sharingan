package cn.moyada.sharingan.monitor.api;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
public class Invocation {

    private String application;

    private String domain;

    private Object args;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Object getArgs() {
        return args;
    }

    public void setArgs(Object args) {
        this.args = args;
    }
}
