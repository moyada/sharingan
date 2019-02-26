package io.moyada.sharingan.serialization.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class AbstractSerializer {

    private static final String NULL = "null";

    protected static final String pattern = "yyyy-MM-dd HH:mm:ss";
    protected final DateFormat dateFormat = new SimpleDateFormat(pattern);

    protected void checkJson(String json) throws SerializationException {
        checkNull(json);
        if (NULL.equals(json)) {
            throw new SerializationException("json is null");
        }
    }

    protected void checkNull(Object obj) throws SerializationException {
        if (null == obj) {
            throw new SerializationException("object is null");
        }
    }
}
