package cn.moyada.sharingan.core.provider;

/**
 * 参数提供器
 */
public interface ArgsProvider {

    /**
     * 生成下一组参数
     * @return
     */
    Object fetchNext();
}
