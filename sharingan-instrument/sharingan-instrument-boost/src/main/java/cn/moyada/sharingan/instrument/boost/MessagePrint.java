package cn.moyada.sharingan.instrument.boost;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import sun.misc.Launcher;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    public static void main(String[] args) throws NoSuchMethodException, NotFoundException, IOException, CannotCompileException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException {

        ClassLoader parent = Launcher.getLauncher().getClassLoader().getParent();

        Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        defineClass.setAccessible(true);

        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get(MessagePrint.class.getName());
        byte[] bytes = ctClass.toBytecode();
        defineClass.invoke(parent, MessagePrint.class.getName(), bytes, 0, bytes.length);

        Class<?> aClass = Class.forName(MessagePrint.class.getName(), true, parent);
        System.out.println(aClass.getClassLoader().toString());
        System.out.println(MessagePrint.class.getClassLoader().toString());
    }
}
