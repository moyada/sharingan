package io.moyada.sharingan.serialization.api;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class SerializationException extends Exception {

    private static final long serialVersionUID = 4347890145965721587L;

    public SerializationException(Throwable cause) {
        super(cause.getMessage(), cause, false, false);
    }

    public SerializationException(String message) {
        super(message, null, false, false);
    }
}
