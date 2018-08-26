package cn.moyada.dubbo.faker.core.model.queue;

import cn.moyada.dubbo.faker.core.model.padding.Bit7Padding;

/**
 * 数据元素
 * @author xueyikang
 * @create 2018-04-06 10:38
 */
public class Node<V> extends Bit7Padding {

    protected volatile V value;

    Node(V value) {
        this.value = value;
    }
}