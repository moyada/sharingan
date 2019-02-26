package io.moyada.sharingan.serialization.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import io.moyada.sharingan.serialization.api.AbstractSerializer;
import io.moyada.sharingan.serialization.api.SerializationException;
import io.moyada.sharingan.serialization.api.Serializer;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class GsonSerializer extends AbstractSerializer implements Serializer {

    private final Gson gson;

    private static final TypeToken<?> MAP_TYPE = new MapTypeToken();

    private static final TypeToken<?> LIST_TYPE = new ListTypeToken();

    public GsonSerializer() {
        gson = new GsonBuilder()
                .setDateFormat(pattern)
                .create();
    }

    @Override
    public String toString(Object obj) throws SerializationException {
        checkNull(obj);

        return gson.toJson(obj);
    }

    @Override
    public String toString(List<?> list) throws SerializationException {
        checkNull(list);

        return gson.toJson(list);
    }

    @Override
    public <T> T toObject(String json, Class<T> clazz) throws SerializationException {
        checkJson(json);

        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> T[] toArray(String json, Class<T[]> clazz) throws SerializationException {
        checkJson(json);

        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> Collection<T> toList(String json, Class<T> clazz) throws SerializationException {
        checkJson(json);

        Object obj;
        try {
            obj = gson.fromJson(json, LIST_TYPE.getType());
        } catch (JsonSyntaxException e) {
            throw new SerializationException(e);
        }

        if (obj instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<T> list = (Collection<T>) obj;
            return list;
        }

        throw new SerializationException("unknown error");
    }

    @Override
    public <T, U> Map<T, U> toMap(String json, Class<T> t, Class<U> u) throws SerializationException {
        checkJson(json);

        Object obj;
        try {
            obj = gson.fromJson(json, MAP_TYPE.getType());
        } catch (JsonSyntaxException e) {
            throw new SerializationException(e);
        }

        if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<T, U> map = (Map<T, U>) obj;
            return map;
        }

        throw new SerializationException("unknown error");
    }

    private static final class MapTypeToken extends TypeToken<Map<String, Object>> {

    }

    private static final class ListTypeToken extends TypeToken<Collection<Object>> {

    }
}
