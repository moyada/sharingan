package cn.moyada.sharingan.config;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface InvokeContextFactory {

    InvokeContext getContext(int appId, int serviceId, int invokeId, String expression);
}
