package cn.moyada.sharingan.rpc.api.invoke;

import cn.moyada.sharingan.common.exception.InstanceNotFountException;

/**
 * 调用代理
 * @author xueyikang
 * @since 1.0
 */
public interface InvokeProxy extends AsyncInvoke {

    /**
     * 初始化环境
     * @param metaDate
     * @throws InstanceNotFountException
     */
    void initialization(InvocationMetaDate metaDate) throws InstanceNotFountException;
}
