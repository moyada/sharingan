package cn.moyada.sharingan.instrument.boost.proxy;

import cn.moyada.sharingan.instrument.boost.common.ProxyMethod;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ASMProxy implements ClassProxy {

    @Override
    public <T> Class<T> wrapper(Class<T> target, List<ProxyMethod> methods) throws Exception {
//        ClassReader classReader = new ClassReader(target.getName());
//        ClassWriter classWriter = new ClassWriter(classReader, 0);

        return target;
    }

//    class FieldAdapter extends ClassVisitor {
//
//        private String fieldName;
//        private String fieldDefault;
//        private int access = org.objectweb.asm.Opcodes.ACC_PRIVATE;
//        private boolean isFieldPresent;
//
//        public FieldAdapter(String fieldName, int fieldAccess, ClassVisitor cv) {
//            super(ASM4, cv);
//            this.cv = cv;
//            this.fieldName = fieldName;
//            this.access = fieldAccess;
//        }
//    }
//
//    class MonitorVisitor extends ClassVisitor {
//
//        private final String methodName;
//
//        public MonitorVisitor(String methodName) {
//            super(ASM4);
//            this.methodName = methodName;
//        }
//
//        @Override
//        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
//            MethodVisitor method = cv.visitMethod(access, name, descriptor, signature, exceptions);
//            if (methodName.equals(name)) {
//                method = new InvokeMethodVisitor(method);
//            }
//
//            return super.visitMethod(access, name, descriptor, signature, exceptions);
//        }
//    }
//
//    class InvokeMethodVisitor extends MethodVisitor {
//
//        public InvokeMethodVisitor(MethodVisitor mv) {
//            super(Opcodes.ASM4, mv);
//        }
//
//        @Override
//        public void visitCode() {
//            //此方法在访问方法的头部时被访问到，仅被访问一次
//            //此处可插入新的指令
//            super.visitCode();
//        }
//
//        @Override
//        public void visitInsn(int opcode) {
//            //此方法可以获取方法中每一条指令的操作类型，被访问多次
//            //如应在方法结尾处添加新指令，则应判断：
//            if (opcode == Opcodes.RETURN) {
//
//            }
//            super.visitInsn(opcode);
//        }
//    }
}
