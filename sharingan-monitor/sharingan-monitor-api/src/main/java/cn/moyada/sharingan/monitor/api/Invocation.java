package cn.moyada.sharingan.monitor.api;

import java.util.Map;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
public interface Invocation {

    Map<String, Object> getArgs();

    void addArgs(String name, Object args);

    void addArgs(String name, boolean args);

    void addArgs(String name, byte args);

    void addArgs(String name, short args);

    void addArgs(String name, int args);

    void addArgs(String name, long args);

    void addArgs(String name, float args);

    void addArgs(String name, double args);

    void addArgs(String name, char args);
}
