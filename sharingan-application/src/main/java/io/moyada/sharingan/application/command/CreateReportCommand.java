package io.moyada.sharingan.application.command;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class CreateReportCommand {

    private int appId;
    private int serviceId;
    private int funcId;

    public CreateReportCommand(int appId, int serviceId, int funcId) {
        this.appId = appId;
        this.serviceId = serviceId;
        this.funcId = funcId;
    }

    public int getAppId() {
        return appId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getFuncId() {
        return funcId;
    }
}
