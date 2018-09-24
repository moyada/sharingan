package cn.moyada.sharingan.core.provider;

import java.util.Random;

/**
 * 随机数值提供器
 * @author xueyikang
 * @since 1.0
 **/
public abstract class RandomProvider extends ReplacementProvider implements ArgsProvider {

    protected final Random random;

    public RandomProvider(String value, Class<?> paramType, String target) {
        super(value, paramType, target);
        this.random = new Random();
    }
}
