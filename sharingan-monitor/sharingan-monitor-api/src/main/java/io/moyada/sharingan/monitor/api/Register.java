package io.moyada.sharingan.monitor.api;

import io.moyada.sharingan.monitor.api.entity.AppInfo;
import io.moyada.sharingan.monitor.api.entity.FunctionInfo;
import io.moyada.sharingan.monitor.api.entity.HttpInfo;
import io.moyada.sharingan.monitor.api.entity.ServiceInfo;
import io.moyada.sharingan.monitor.api.exception.RegisterException;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface Register {

    String TYPE = "null";

    int addAppReturnId(AppInfo appInfo) throws RegisterException;

    int addServiceReturnId(ServiceInfo serviceInfo) throws RegisterException;

    int addFunctionReturnId(FunctionInfo functionInfo) throws RegisterException;

    int addHttpReturnId(HttpInfo httpInfo) throws RegisterException;
}
