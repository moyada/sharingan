package cn.moyada.sharingan.config;

import cn.moyada.sharingan.module.Dependency;
import cn.moyada.sharingan.module.InvokeInfo;
import cn.moyada.sharingan.storage.api.domain.AppDO;
import cn.moyada.sharingan.storage.api.domain.FunctionDO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类型转换器
 * @author xueyikang
 * @since 0.0.1
 **/
public class ContextConverter {

    /**
     * 获取方法调用信息
     * @param functionDO
     * @return
     */
    public static InvokeInfo toInvokeInfo(FunctionDO functionDO) {
        InvokeInfo invokeInfo = new InvokeInfo();
        invokeInfo.setClassType(functionDO.getClassName());
        invokeInfo.setMethodName(functionDO.getMethodName());
        invokeInfo.setParamType(functionDO.getParamType());
        invokeInfo.setReturnType(functionDO.getReturnType());
        return invokeInfo;
    }

    /**
     * 获取依赖信息
     * @param infoDO
     * @return
     */
    public static Dependency toDependency(AppDO infoDO) {
        Dependency dependency = new Dependency();
        dependency.setArtifactId(infoDO.getArtifactId());
        dependency.setGroupId(infoDO.getGroupId());
        dependency.setUrl(infoDO.getUrl());
        dependency.setVersion(infoDO.getVersion());
        return dependency;
    }

    /**
     * 获取依赖信息
     * @param infoDOs
     * @return
     */
    public static List<Dependency> toDependency(List<AppDO> infoDOs) {
        if (null == infoDOs) {
            return null;
        }
        return infoDOs.stream().map(ContextConverter::toDependency).collect(Collectors.toList());
    }
}
