package io.moyada.sharingan.monitor.api.entity;

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
     * 以 json 串形式保存
     */
    JSON,
    ;
}
