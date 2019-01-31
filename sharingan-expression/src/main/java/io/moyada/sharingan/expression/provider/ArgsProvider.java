package io.moyada.sharingan.expression.provider;


/**
 * 参数提供器
 */
public interface ArgsProvider {

    /**
     * 生成下一组参数
     * @return
     */
    Object fetchNext();

    /**
     * 生成参数替换数据
     * @param source
     * @return
     */
    String replace(String source);
}
