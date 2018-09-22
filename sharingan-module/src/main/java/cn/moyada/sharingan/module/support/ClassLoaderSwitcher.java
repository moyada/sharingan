package cn.moyada.sharingan.module.support;

import cn.moyada.sharingan.module.Dependency;

/**
 * 类加载器切换器
 * @author xueyikang
 * @since 1.0
 **/
public interface ClassLoaderSwitcher {

    /**
     * 切换加载器
     * @param dependency
     */
    void checkout(Dependency dependency);

    /**
     * 恢复加载器
     */
    void recover();
}
