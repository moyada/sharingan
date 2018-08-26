package cn.moyada.faker.module.fetch;

import cn.moyada.faker.module.Dependency;

public interface DependencyFetch {

    String getJarUrl(Dependency dependency);
}
