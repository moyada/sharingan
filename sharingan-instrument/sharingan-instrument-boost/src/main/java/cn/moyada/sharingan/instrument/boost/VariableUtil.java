package cn.moyada.sharingan.instrument.boost;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class VariableUtil {

    public static String[] getNameByASM(Class clazz, Method method) throws IOException {
//        final Class<?>[] methodParameterTypes = method.getParameterTypes();
//        final int methodParameterCount = methodParameterTypes.length;
//        final boolean isStatic = Modifier.isStatic(method.getModifiers());
//
//        // 读取字节码信息到ClassReader中
//        ClassReader reader = new ClassReader(clazz.getName());
//        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//
//        String[] variableName = new String[methodParameterCount];
//
//        //accept接收了一个ClassAdapter的子类，想要操作什么，就在子类实现什么
//        reader.accept(new ClassAdapter(cw) {
//            /**
//             * 会遍历该类的所有方法，你可以对不需要操作的方法直接返回
//             */
//            @Override
//            public MethodVisitor visitMethod(final int access, final String name, final String desc,
//                                             final String signature, final String[] exceptions) {
//
//                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
//
//                final Type[] argTypes = Type.getArgumentTypes(desc);
//
//                //不需要操作的方法，直接返回，注意不要返回null,会把该方法删掉
//                if (!name.equals(method.getName()) || !matchTypes(argTypes, methodParameterTypes)) {
//                    return mv;
//                }
//
//                /**
//                 *  遍历该方法信息，比如参数、注解等，这里我们要操作参数，所以实现了参数方法
//                 */
//                return new MethodAdapter(mv) {
//
//                    @Override
//                    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
//                        //如果是静态方法，第一个参数就是方法参数，非静态方法，则第一个参数是 this ,然后才是方法的参数
//                        int methodParameterIndex = isStatic ? index : index - 1;
//                        if (0 <= methodParameterIndex && methodParameterIndex < methodParameterCount) {
//                            variableName[methodParameterIndex] = name;
//                        }
//                        super.visitLocalVariable(name, desc, signature, start, end, index);
//                    }
//                };
//            }
//        }, 0);
//
//        return variableName;
        return null;
    }

    /**
     * 比较参数是否一致
     */
//    private static boolean matchTypes(Type[] types, Class<?>[] parameterTypes) {
//        if (types.length != parameterTypes.length) {
//            return false;
//        }
//        for (int i = 0; i < types.length; i++) {
//            if (!Type.getType(parameterTypes[i]).equals(types[i])) {
//                return false;
//            }
//        }
//        return true;
//    }

    public static String[] getNameByJavassist(Class clazz, Method method) throws NotFoundException {
        ClassPool classPool = ClassPool.getDefault();

        CtClass ctClass = classPool.get(clazz.getName());

        int count = method.getParameterCount();
        Class<?>[] paramTypes = method.getParameterTypes();
        CtClass[] ctParams = new CtClass[count];
        for (int i = 0; i < count; i++) {
            ctParams[i] = classPool.getCtClass(paramTypes[i].getName());
        }

        CtMethod ctMethod = ctClass.getDeclaredMethod(method.getName(), ctParams);

        //得到该方法信息类
        MethodInfo methodInfo = ctMethod.getMethodInfo();

        //获取属性变量相关
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        if (codeAttribute == null) {
            return null;
        }

        //获取方法本地变量信息，包括方法声明和方法体内的变量
        //需注意，若方法为非静态方法，则第一个变量名为this
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);

        String[] variableName = new String[count];
        int index = 0;
        int pos = 0;
        if (!Modifier.isStatic(method.getModifiers())) {
            count++;
            pos++;
        }

        for (int i = pos; i < count; i++) {
            variableName[index++]= attr.variableName(i);
        }

        return variableName;
    }
}
