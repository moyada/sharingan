package cn.xueyikang.dubbo.faker.core.utils;

import com.google.common.collect.Lists;
import com.google.gson.*;

import java.util.List;

/**
 * Created by xueyikang on 2017/8/6.
 */
public class JsonUtil {

    private static final Gson gson = new GsonBuilder().create();
    private static final JsonParser parser = new JsonParser();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }

    public static List<Object> fromJson(String json, List<Class<?>> paramType) {
        List<Object> list = Lists.newArrayListWithExpectedSize(paramType.size());
        JsonArray array = parser.parse(json).getAsJsonArray();
        int index = 0;
        for(final JsonElement elem : array){
            list.add(gson.fromJson(elem, paramType.get(index++)));
        }
        return list;
    }
}
