package cn.moyada.dubbo.faker.core.instrument;

import java.lang.instrument.Instrumentation;

/**
 * @author xueyikang
 * @create 2018-04-29 23:14
 */
public class Main {

    public static void premain(String args, Instrumentation inst) {
        inst.addTransformer(new ProxyTransformer());
    }
}
