package cn.moyada.sharingan.instrument.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MonitorAgent {

    public static void premain(String args, Instrumentation instrumentation) {

    }

    class MonitorClassFileTransformer implements ClassFileTransformer {

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            return new byte[0];
        }
    }
}
