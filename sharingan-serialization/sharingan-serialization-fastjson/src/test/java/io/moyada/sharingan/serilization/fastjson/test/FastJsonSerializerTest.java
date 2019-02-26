package io.moyada.sharingan.serilization.fastjson.test;

import io.moyada.sharingan.serialization.api.Serializer;
import io.moyada.sharingan.serialization.fastjson.FastJsonSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class FastJsonSerializerTest {

    private static Serializer serializer;

    @BeforeAll
    public static void setup() {
        serializer = new FastJsonSerializer();
    }

    @Test
    public void primitiveTest() throws Exception {
        String json;

        json = serializer.toString(-20);
        Integer integer = serializer.toObject(json, Integer.class);
        Assertions.assertEquals(integer.intValue(), -20);

        json = serializer.toString(true);
        Boolean bool = serializer.toObject(json, Boolean.class);
        Assertions.assertEquals(bool, true);

        json = serializer.toString('c');
        char c = serializer.toObject(json, char.class);
        Assertions.assertEquals(c, 'c');
    }

    @Test
    public void compositeTest() throws Exception {
        String json;

        int[] ints1 = {2, 1, 2};
        json = serializer.toString(ints1);
        int[] ints2 = serializer.toObject(json, int[].class);
        Assertions.assertArrayEquals(ints1, ints2);

        json = serializer.toString(Type.A);
        Type type = serializer.toObject(json, Type.class);
        Assertions.assertEquals(type, Type.A);

        Collection<Object> list1 = new ArrayList<>();
        list1.add("a");
        list1.add(123);
        list1.add(true);

        json = serializer.toString(list1);
        Collection<Object> list2 = serializer.toList(json, Object.class);
        Assertions.assertEquals(list1, list2);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", 123);
        map1.put("name", "nick");
        map1.put("project", "test");
        map1.put("flag", false);
        json = serializer.toString(map1);
        Map<String, Object> map2 = serializer.toMap(json, String.class, Object.class);
        Assertions.assertEquals(map1, map2);

        Bean bean = new Bean();
        bean.setType(Type.B);
        bean.setId(100);
        bean.setName("bean");
        bean.setFlag(Collections.singletonList(true));
        bean.setAttach(map1);

        json = serializer.toString(bean);
        Bean bean2 = serializer.toObject(json, Bean.class);
        Assertions.assertEquals(bean, bean2);
    }

    @Test
    public void objectTest() throws Exception {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", 123);
        map1.put("name", "nick");
        map1.put("project", "test");
        map1.put("flag", false);


        Bean bean = new Bean();
        bean.setType(Type.B);
        bean.setId(100);
        bean.setName("bean");
        bean.setFlag(Collections.singletonList(true));
        bean.setAttach(map1);

        String json = serializer.toString(bean);
        Bean bean2 = serializer.toObject(json, Bean.class);
        Assertions.assertEquals(bean, bean2);
    }

    @Test
    public void dateTest() throws Exception {
        DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = DATE_FORMAT.parse("2018-11-11 12:31:23");

        String json;

        json = serializer.toString(date);
        java.util.Date obj = serializer.toObject(json, java.util.Date.class);
        Assertions.assertEquals(date, obj);

        Date date1 = new Date(date.getTime());
        json = serializer.toString(date1);
        java.util.Date date2 = serializer.toObject(json, Date.class);
        Assertions.assertEquals(date1, date2);

        Timestamp timestamp1 = new Timestamp(date.getTime());
        json = serializer.toString(timestamp1);
        Timestamp timestamp2 = serializer.toObject(json, Timestamp.class);
        Assertions.assertEquals(timestamp1, timestamp2);
    }
}
