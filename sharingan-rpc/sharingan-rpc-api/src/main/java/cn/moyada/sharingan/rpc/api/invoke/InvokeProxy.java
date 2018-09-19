package cn.moyada.sharingan.rpc.api.invoke;

import cn.moyada.sharingan.common.exception.InstanceNotFountException;

public interface InvokeProxy extends AsyncInvoke {

    void initialization(InvocationMetaDate metaDate) throws InstanceNotFountException;
}
