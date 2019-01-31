package io.moyada.sharingan.application;

import io.moyada.sharingan.domain.metadada.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Service
public class ConfigService {

    private MetadataRepository metadataRepository;

    @Autowired
    public ConfigService(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    public List<AppData> getApp() {
        return metadataRepository.findAllApp();
    }

    public List<ServiceData> getService(int appId) {
        return metadataRepository.findServiceByApp(appId);
    }

    public List<FunctionData> getFunction(int serviceId) {
        List<MethodData> methodData = metadataRepository.findMethodByService(serviceId);
        List<HttpData> httpData = metadataRepository.findHttpByService(serviceId);

        if (methodData.isEmpty() && httpData.isEmpty()) {
            return Collections.emptyList();
        }

        List<FunctionData> data = new ArrayList<>(methodData.size() + httpData.size());
        if (!methodData.isEmpty()) {
            data.addAll(methodData.stream()
                    .map(c -> new FunctionData(c.getId(), c.getMethodName(), c.getParamType() + ":" + c.getReturnType(), c.getExpression()))
                    .collect(Collectors.toList()));
        }

        if (!httpData.isEmpty()) {
            data.addAll(httpData.stream()
                    .map(c -> new FunctionData(c.getId(), c.getMethodName(),
                            c.getMethodType() +  " [" + c.getParam() + "], [" + c.getHeader() + "]", c.getExpression()))
                    .collect(Collectors.toList()));
        }

        return data;
    }

}
