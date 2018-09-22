package cn.moyada.sharingan.module.fetch;

import cn.moyada.sharingan.module.Dependency;

/**
 * jar包获取器
 */
public interface DependencyFetch {

    /**
     * 通过版本仓库信息获取jar包链接
     * @param dependency
     * @return
     */
    String getJarUrl(Dependency dependency);
}
