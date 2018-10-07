package cn.moyada.sharingan.core.support;

import cn.moyada.sharingan.core.provider.ArgsProvider;

/**
 * 参数提供者包装
 * @author xueyikang
 * @since 0.0.1
 */
public class ArgsProviderContainer {

    /**
     * 参数提供者
     */
    private ArgsProvider[] providers;

    /**
     * 参数长度
     */
    private int length;

    /**
     * 生成下个参数
     * @return
     */
    public Object[] getArgs() {
        if (null == providers) {
            return null;
        }

        Object[] args = new Object[length];
        for (int index = 0; index < length; index++) {
            args[index] = providers[index].fetchNext();
        }

        return args;
    }

    /**
     * 无参方法参数提供器
     * @return
     */
    public static ArgsProviderContainer emptyContainer() {
        ArgsProviderContainer container = new ArgsProviderContainer();
        return container;
    }

    public void setProviders(ArgsProvider[] providers) {
        this.providers = providers;
        this.length = providers.length;
    }
}
