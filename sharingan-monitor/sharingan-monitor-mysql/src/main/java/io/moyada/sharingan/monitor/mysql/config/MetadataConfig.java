package io.moyada.sharingan.monitor.mysql.config;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MetadataConfig {

    private AppConfig appConfig;

    private ServiceConfig serviceConfig;

    private FunctionConfig functionConfig;

    private HttpConfig httpConfig;

    public MetadataConfig(AppConfig appConfig,
                          ServiceConfig serviceConfig,
                          FunctionConfig functionConfig,
                          HttpConfig httpConfig) {
        this.appConfig = appConfig;
        this.serviceConfig = serviceConfig;
        this.functionConfig = functionConfig;
        this.httpConfig = httpConfig;
    }

    public void checkParam() {
        if (null == appConfig) {
            throw new NullPointerException("appConfig is null");
        }
        if (null == serviceConfig) {
            throw new NullPointerException("serviceConfig is null");
        }
        if (null == functionConfig) {
            throw new NullPointerException("functionConfig is null");
        }
        if (null == httpConfig) {
            throw new NullPointerException("httpConfig is null");
        }
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public ServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    public FunctionConfig getFunctionConfig() {
        return functionConfig;
    }

    public HttpConfig getHttpConfig() {
        return httpConfig;
    }
}
