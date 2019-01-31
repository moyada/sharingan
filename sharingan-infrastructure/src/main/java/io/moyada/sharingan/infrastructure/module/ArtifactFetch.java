package io.moyada.sharingan.infrastructure.module;

/**
 * jar包获取器
 */
public interface ArtifactFetch {

    /**
     * 通过版本仓库信息获取jar包链接
     * @param dependency
     * @return
     */
    String getJarUrl(Dependency dependency);
}
