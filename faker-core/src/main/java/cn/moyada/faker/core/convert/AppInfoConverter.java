package cn.moyada.faker.core.convert;

import cn.moyada.faker.manager.domain.AppInfoDO;
import cn.moyada.faker.module.Dependency;

import java.util.List;
import java.util.stream.Collectors;

public class AppInfoConverter {

    public static Dependency toDependency(AppInfoDO infoDO) {
        Dependency dependency = new Dependency();
        dependency.setArtifactId(infoDO.getArtifactId());
        dependency.setGroupId(infoDO.getGroupId());
        dependency.setUrl(infoDO.getUrl());
        dependency.setVersion(infoDO.getVersion());
        return dependency;
    }

    public static List<Dependency> toDependency(List<AppInfoDO> infoDOs) {
        if (null == infoDOs) {
            return null;
        }
        return infoDOs.stream().map(AppInfoConverter::toDependency).collect(Collectors.toList());
    }
}
