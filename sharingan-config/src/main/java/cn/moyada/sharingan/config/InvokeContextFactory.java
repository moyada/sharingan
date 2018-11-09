package cn.moyada.sharingan.config;

/**
 * 调用信息工厂
 * @author xueyikang
 * @since 0.0.1
 **/
public interface InvokeContextFactory {

    /**
     * 获取配置
     * @param appId
     * @param serviceId
     * @param invokeId
     * @param expression
     * @return
     */
    InvokeContext getContext(int appId, int serviceId, int invokeId, String expression);
}
