package cn.moyada.sharingan.core.support;

public class ArgsProviderContainer {

    private ArgsProvider[] providers;

    private int length;

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

    public static ArgsProviderContainer emptyContainer() {
        ArgsProviderContainer container = new ArgsProviderContainer();
        return container;
    }

    public void setProviders(ArgsProvider[] providers) {
        this.providers = providers;
        this.length = providers.length;
    }
}
