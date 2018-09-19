package cn.moyada.sharingan.core.convert;

import cn.moyada.sharingan.module.InvokeInfo;
import cn.moyada.sharingan.storage.api.domain.FunctionDO;

public class FunctionInfoConverter {

    public static InvokeInfo getInvokeInfo(FunctionDO functionDO) {
        InvokeInfo invokeInfo = new InvokeInfo();
        invokeInfo.setClassType(functionDO.getClassName());
        invokeInfo.setMethodName(functionDO.getMethodName());
        invokeInfo.setParamType(functionDO.getParamType());
        invokeInfo.setReturnType(functionDO.getReturnType());
        return invokeInfo;
    }
}
