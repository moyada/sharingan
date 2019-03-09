package io.moyada.sharingan.expression;


import io.moyada.sharingan.domain.expression.ParamProvider;
import io.moyada.sharingan.expression.provider.ArgsProvider;

/**
 * 参数提供者包装
 * @author xueyikang
 * @since 0.0.1
 */
public class ExpressionParamProvider implements ParamProvider {

    /**
     * 参数提供者
     */
    private ArgsProvider[] providers;

    /**
     * 参数长度
     */
    private int length;

    ExpressionParamProvider(ArgsProvider[] providers) {
        this.providers = providers;
        this.length = providers.length;
    }

    /**
     * 生成下个参数
     * @return
     */
    @Override
    public Object[] getNext() {
        Object[] args = new Object[length];
        for (int index = 0; index < length; index++) {
            args[index] = providers[index].fetchNext();
        }
        return args;
    }
}
