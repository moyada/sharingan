package cn.moyada.dubbo.faker.core.parser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xueyikang on 2017/12/15.
 */
public abstract class JsonParser {

    public abstract String toJson(Object obj);

    public abstract <C> C toObject(String json, Class<C> c);

    public abstract <C> C[] toArray(String json, Class<C> c);

    public abstract <C> List<C> toList(String json, Class<C> c);

    public abstract <T, U> Map<T, U> toMap(String json, Class<T> t, Class<U> u);

    public abstract <T, U> LinkedHashMap<T, List<U>> toLinkedMapList(String json, Class<T> t, Class<U> u);

    public abstract <T, U> Map<T, List<U>> toMapList(String json, Class<T> t, Class<U> u);
}
