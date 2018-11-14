package cn.moyada.sharingan.monitor.api.entity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 标准调用数据
 * @author xueyikang
 * @since 0.0.1
 **/
public class DefaultInvocation implements Invocation {

    private String application;

    private String domain;

    private String protocol;

    private SerializationType serializationType;

    private Map<String, Object> args;

    private Map<String, Object> attach;

    @Override
    public String getApplication() {
        return application;
    }

    @Override
    public void setApplication(String application) {
        this.application = application;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public Map<String, Object> getAttach() {
        return attach;
    }

    @Override
    public void addAttach(String name, Object value) {
        if (attach == null) {
            attach = new HashMap<>();
        }
        attach.put(name, value);
    }

    @Override
    public SerializationType getSerializationType() {
        return serializationType;
    }

    @Override
    public void setSerializationType(SerializationType serializationType) {
        this.serializationType = serializationType;
    }

    @Override
    public Map<String, Object> getArgs() {
        return args;
    }

    @Override
    public void addArgs(String name, Object args) {
        if (this.args == null) {
            this.args = new HashMap<>();
        }

        this.args.put(name, args);
    }

    @Override
    public void addArgs(String name, byte args) {
        if (this.args == null) {
            this.args = new LinkedHashMap<>();
        }

        this.args.put(name, args);
    }

    @Override
    public void addArgs(String name, short args) {
        addArgs(name, (Object) args);
    }

    @Override
    public void addArgs(String name, int args) {
        addArgs(name, (Object) args);
    }

    @Override
    public void addArgs(String name, long args) {
        addArgs(name, (Object) args);
    }

    @Override
    public void addArgs(String name, boolean args) {
        addArgs(name, (Object) args);
    }

    @Override
    public void addArgs(String name, char args) {
        addArgs(name, (Object) args);
    }

    @Override
    public void addArgs(String name, double args) {
        addArgs(name, (Object) args);
    }

    @Override
    public void addArgs(String name, float args) {
        addArgs(name, (Object) args);
    }

    @Override
    public String toString() {
        return "DefaultInvocation{" +
                "application='" + application + '\'' +
                ", domain='" + domain + '\'' +
                ", protocol='" + protocol + '\'' +
                ", args=" + args +
                ", attach=" + attach +
                '}';
    }
}
