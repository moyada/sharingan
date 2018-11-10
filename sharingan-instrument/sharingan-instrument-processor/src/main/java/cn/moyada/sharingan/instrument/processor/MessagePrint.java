package cn.moyada.sharingan.instrument.processor;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MessagePrint {

    private final Messager messager;

    public MessagePrint(Messager messager) {
        this.messager = messager;
    }

    public void info(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    public void warning(String msg) {
        messager.printMessage(Diagnostic.Kind.WARNING, msg);
    }

    public void error(String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR, msg);
    }
}
