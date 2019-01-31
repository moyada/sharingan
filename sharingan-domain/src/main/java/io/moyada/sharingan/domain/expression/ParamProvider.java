package io.moyada.sharingan.domain.expression;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface ParamProvider {

    // 无参方法参数提供器
    ParamProvider EMPTY_PARAM = new ParamProvider() {
        @Override
        public Object[] getNext() {
            return null;
        }
    };

    Object[] getNext();
}
