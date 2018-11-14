package cn.moyada.sharingan.monitor.api.entity;

/**
 * 参数序列化方式
 * @author xueyikang
 * @since 1.0
 **/
public enum SerializationType {

    /**
     * 只保存参数值
     */
    VALUE,

    /**
     * 保存参数名与参数值
     */
    OBJECT,
    ;
}
