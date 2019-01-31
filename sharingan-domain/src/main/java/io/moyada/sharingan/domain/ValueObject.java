package io.moyada.sharingan.domain;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class ValueObject<T> {

    public boolean asEquals(T obj) {
        if (this.hashCode() != obj.hashCode()) {
            return false;
        }
        return this.equals(obj);
    }
}
