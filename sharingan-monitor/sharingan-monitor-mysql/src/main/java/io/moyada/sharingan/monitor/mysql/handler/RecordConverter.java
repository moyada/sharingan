package io.moyada.sharingan.monitor.mysql.handler;

import io.moyada.sharingan.serialization.api.Serializer;
import io.moyada.sharingan.monitor.api.entity.Invocation;
import io.moyada.sharingan.monitor.api.entity.SerializationType;
import io.moyada.sharingan.monitor.api.handler.InvocationConverter;
import io.moyada.sharingan.monitor.mysql.data.Record;

import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class RecordConverter implements InvocationConverter<Record> {

    private DataRepository dataRepository;

    private Serializer serializer;

    public RecordConverter(DataRepository dataRepository, Serializer serializer) {
        this.dataRepository = dataRepository;
        this.serializer = serializer;
    }

    @Override
    public Record convert(Invocation invocation) {
        Integer appId = dataRepository.getAppId(invocation.getApplication());
        if (null == appId) {
            return null;
        }
        String domain = invocation.getDomain();
        if (null == domain) {
            return null;
        }
        Map<String, Object> args = invocation.getArgs();
        if (null == args) {
            return null;
        }
        SerializationType serializationType = invocation.getSerializationType();
        if (null == serializationType) {
            return null;
        }

        String param;
        if (serializationType == SerializationType.VALUE && args.size() == 1) {
            Object value = args.values().iterator().next();
            if (null == value) {
                return null;
            }
            try {
                param = serializer.toString(value);
            } catch (Exception e) {
                return null;
            }
        } else {
            try {
                param = serializer.toString(invocation.getArgs());
            } catch (Exception e) {
                return null;
            }
        }

        Record record = new Record();
        record.setAppId(appId);
        record.setDomain(domain);
        record.setParamValue(param);
        return record;
    }
}
