package io.moyada.sharingan.serialization.gson.test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class Bean {

    private Type type;

    private int id;

    private String name;

    private List<Boolean> flag;

    private Map<String, Object> attach;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bean bean = (Bean) o;
        return id == bean.id &&
                type == bean.type &&
                Objects.equals(name, bean.name) &&
                Objects.equals(flag, bean.flag) &&
                Objects.equals(attach, bean.attach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, name, flag, attach);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Boolean> getFlag() {
        return flag;
    }

    public void setFlag(List<Boolean> flag) {
        this.flag = flag;
    }

    public Map<String, Object> getAttach() {
        return attach;
    }

    public void setAttach(Map<String, Object> attach) {
        this.attach = attach;
    }
}
