package cn.moyada.sharingan.monitor.mysql;

import cn.moyada.sharingan.monitor.api.entity.*;
import cn.moyada.sharingan.monitor.api.receiver.InvocationReceiver;
import cn.moyada.sharingan.serialization.api.Serializer;
import cn.moyada.sharingan.serialization.jackson.JacksonSerializer;

import java.util.Collection;
import java.util.Map;

/**
 * json 序列化接收器
 * @author xueyikang
 * @since 1.0
 **/
public class JsonInvocationReceiver implements InvocationReceiver<Record<String>> {

    private final Serializer serializer;

    public JsonInvocationReceiver() {
        serializer = new JacksonSerializer();
    }

    @Override
    public Record<String> receive(Invocation invocation) {
        DefaultRecord record = new DefaultRecord();
        record.setApplication(invocation.getApplication());
        record.setDomain(invocation.getDomain());
        record.setProtocol(invocation.getProtocol());

        Map<String, Object> args = invocation.getArgs();
        SerializationType serializationType = invocation.getSerializationType();

        if (null == serializationType) {
            record.setArgs(getArrayValue(args));
        } else {
            switch (serializationType) {
                case VALUE:
                    record.setArgs(getArrayValue(args));
                    break;
                case OBJECT:
                    try {
                        record.setArgs(serializer.toString(args));
                    } catch (Exception e) {
                        record.setArgs(args.toString());
                    }
                    break;
            }
        }

        return record;
    }

    /**
     * 解析数值
     * @param args
     * @return
     */
    private String getArrayValue(Map<String, Object> args) {
        int size = args.size();
        if (0 == size) {
            return null;
        }

        Collection<Object> values = args.values();
        if (1 == size) {
            Object next = values.iterator().next();
            try {
                return serializer.toString(next);
            } catch (Exception e) {
                return next.toString();
            }
        }

        try {
            return serializer.toString(args.values());
        } catch (Exception e) {
            return args.toString();
        }
    }

    public static void main(String[] args) {
        JsonInvocationReceiver jsonInvocationReceiver = new JsonInvocationReceiver();
        Invocation invocation = new DefaultInvocation();
        invocation.setApplication("test");
        invocation.setDomain("app");
        invocation.setProtocol("dubbo");
        invocation.addAttach("name", "nick");
        invocation.addArgs("name", "nick");
//        invocation.addArgs("age", 24);
        invocation.setSerializationType(SerializationType.VALUE);
        Record<String> receive = jsonInvocationReceiver.receive(invocation);
        System.out.println(receive.getArgs());
    }
}
