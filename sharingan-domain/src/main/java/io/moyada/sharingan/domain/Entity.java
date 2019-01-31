package io.moyada.sharingan.domain;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class Entity<T> {

    private T version;

    public Entity(T version) {
        setVersion(version);
    }

    private void setVersion(T version) {
        this.version = version;
    }

    public T getVersion() {
        return version;
    }
}
