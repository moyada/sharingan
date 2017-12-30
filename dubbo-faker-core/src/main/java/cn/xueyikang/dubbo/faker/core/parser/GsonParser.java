package cn.xueyikang.dubbo.faker.core.parser;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by xueyikang on 2017/12/15.
 */
public class GsonParser extends cn.xueyikang.dubbo.faker.core.parser.JsonParser {
    private static final Logger log = LoggerFactory.getLogger(GsonParser.class);

    private final Gson gson;
    private final JsonParser parser;

    public GsonParser() {
        GsonBuilder builder = new GsonBuilder();
        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        gson = builder.create();
        parser = new JsonParser();
    }

    private final <C> C toObject(JsonElement ele, Class<C> c) {
        C obj;
        try  {
           obj = gson.fromJson(ele, c);
        } catch (Exception e) {
            log.error("Deserialization Object Error: " + e);
            return null;
        }
        return obj;
    }

    @Override
    public String toJson(Object obj) {
        return gson.toJson(obj);
    }

    @Override
    public <C> C toObject(String json, Class<C> c) {
        C obj;
        try  {
            obj = gson.fromJson(json, c);
        } catch (Exception e) {
            log.error("Deserialization Object Error: " + e);
            return null;
        }
        return obj;
    }

    @Override
    public <C> C[] toArray(String json, Class<C> c) {
        return null;
    }

    @Override
    public <C> List<C> toList(String json, Class<C> c) {
        JsonArray jsonArray;
        try {
            jsonArray = parser.parse(json).getAsJsonArray();
        }
        catch (IllegalStateException e) {
            log.error("Deserialization List Error: " + e);
            return null;
        }
        int length = jsonArray.size();
        if(0 == length) {
            return Collections.emptyList();
        }
        C value;
        List<C> list = Lists.newArrayListWithExpectedSize(length);
        for(int index = 0; index < length; index++) {
            value = this.toObject(jsonArray.get(index), c);
            if(null == value) {
                continue;
            }
            list.add(value);
        }
        return list;
    }

    @Override
    public <T, U> Map<T, U> toMap(String json, Class<T> t, Class<U> u) {
        JsonObject jsonObject;
        try {
            jsonObject = parser.parse(json).getAsJsonObject();
        }
        catch (IllegalStateException e) {
            log.error("Deserialization Map Error: " + e);
            return null;
        }
        Set<Map.Entry<String, JsonElement>> jsonSet = jsonObject.entrySet();
        int length = jsonSet.size();
        if(0 == length) {
            return Collections.emptyMap();
        }
        T key;
        U value;
        Map<T, U> resultMap = Maps.newHashMapWithExpectedSize(length);
        for(Map.Entry<String, JsonElement> item : jsonSet) {
            key = t.cast(item.getKey());
            value = this.toObject(item.getValue(), u);
            if(null == value) {
                continue;
            }
            resultMap.put(key, value);
        }
        return resultMap;
    }

    @Override
    public <T, U> LinkedHashMap<T, List<U>> toLinkedMapList(String json, Class<T> t, Class<U> u) {
        JsonObject jsonObject;
        try {
            jsonObject = parser.parse(json).getAsJsonObject();
        }
        catch (IllegalStateException e) {
            log.error("Deserialization LinkedHashMapList Error: " + e);
            return null;
        }
        Set<Map.Entry<String, JsonElement>> jsonSet = jsonObject.entrySet();
        int length = jsonSet.size();
        if(0 == length) {
            return new LinkedHashMap<>(0);
        }
        T key;
        U item;
        List<U> value;
        JsonArray jsonArray;
        int index;
        LinkedHashMap<T, List<U>> resultMap = new LinkedHashMap<>(length);
        for(Map.Entry<String, JsonElement> entry : jsonSet) {
            key = t.cast(entry.getKey());
            jsonArray = entry.getValue().getAsJsonArray();
            length = jsonArray.size();
            if(0 == length) {
                value = Collections.emptyList();
            }
            else {
                value = Lists.newArrayListWithExpectedSize(length);
                for (index = 0; index < length; index++) {
                    item = this.toObject(jsonArray.get(index), u);
                    if(null == item) {
                        continue;
                    }
                    value.add(item);
                }
            }
            resultMap.put(key, value);
        }
        return resultMap;
    }

    @Override
    public <T, U> Map<T, List<U>> toMapList(String json, Class<T> t, Class<U> u) {
        JsonObject jsonObject;
        try {
            jsonObject = parser.parse(json).getAsJsonObject();
        }
        catch (IllegalStateException e) {
            log.error("Deserialization HashMapList Error: " + e);
            return null;
        }
        Set<Map.Entry<String, JsonElement>> jsonSet = jsonObject.entrySet();
        int length = jsonSet.size();
        if(0 == length) {
            return Collections.emptyMap();
        }
        T key;
        U item;
        List<U> value;
        JsonArray jsonArray;
        int index;
        Map<T, List<U>> resultMap = Maps.newHashMapWithExpectedSize(length);
        for(Map.Entry<String, JsonElement> entry : jsonSet) {
            key = t.cast(entry.getKey());
            jsonArray = entry.getValue().getAsJsonArray();
            length = jsonArray.size();
            if(0 == length) {
                value = Collections.emptyList();
            }
            else {
                value = Lists.newArrayListWithExpectedSize(length);
                for (index = 0; index < length; index++) {
                    item = this.toObject(jsonArray.get(index), u);
                    if(null == item) {
                        continue;
                    }
                    value.add(item);
                }
            }
            resultMap.put(key, value);
        }
        return resultMap;
    }
}
