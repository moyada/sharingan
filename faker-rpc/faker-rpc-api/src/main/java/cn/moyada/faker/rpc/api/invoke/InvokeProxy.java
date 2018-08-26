package cn.moyada.faker.rpc.api.invoke;

import cn.moyada.faker.common.exception.InstanceNotFountException;

public interface InvokeProxy {

    void prepare(InvocationMetaDate metaDate) throws InstanceNotFountException;
}
