package cn.moyada.sharingan.module.fetch;

import cn.moyada.sharingan.module.Dependency;

public interface DependencyFetch {

    String getJarUrl(Dependency dependency);
}
