package io.moyada.sharingan.domain.expression;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface ProviderFactory {

    ParamProvider getParamProvider(String[] params, Class<?>[] paramTypes, boolean isRandom);
}
