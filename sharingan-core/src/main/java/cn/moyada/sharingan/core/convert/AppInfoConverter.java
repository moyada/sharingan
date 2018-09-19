package cn.moyada.sharingan.core.convert;

import cn.moyada.sharingan.module.Dependency;
import cn.moyada.sharingan.storage.api.domain.AppDO;

import java.util.List;
import java.util.stream.Collectors;

public class AppInfoConverter {

    public static Dependency toDependency(AppDO infoDO) {
        Dependency dependency = new Dependency();
        dependency.setArtifactId(infoDO.getArtifactId());
        dependency.setGroupId(infoDO.getGroupId());
        dependency.setUrl(infoDO.getUrl());
        dependency.setVersion(infoDO.getVersion());
        return dependency;
    }

    public static List<Dependency> toDependency(List<AppDO> infoDOs) {
        if (null == infoDOs) {
            return null;
        }
        return infoDOs.stream().map(AppInfoConverter::toDependency).collect(Collectors.toList());
    }
}
