package cn.moyada.sharingan.monitor.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
public class DefaultInvocation implements Invocation {

    private Map<String, Object> args;

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
            this.args = new HashMap<>();
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
}
