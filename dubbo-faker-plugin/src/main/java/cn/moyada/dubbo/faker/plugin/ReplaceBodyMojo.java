package cn.moyada.dubbo.faker.plugin;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;

/**
 * @author xueyikang
 * @create 2018-04-01 18:49
 */
@Mojo(name = "replaceBody")
public class ReplaceBodyMojo extends AbstractMojo {

    @Parameter(property = "className", defaultValue = "")
    private String className;

    @Parameter(property = "method", defaultValue = "")
    private String method;

    @Parameter(property = "param", defaultValue = "")
    private String param;

    @Parameter(property = "body", defaultValue = "")
    private String body;

    @Override
    public void execute() {
        ClassPool classPool = ClassPool.getDefault();
        try {
            CtClass ctClass = classPool.get(className);

            String[] params = param.split(",");
            CtClass[] paramTypes = classPool.get(params);
            CtMethod ctMethod = ctClass.getDeclaredMethod(method, paramTypes);

            ctMethod.instrument(
                    new ExprEditor() {
                        public void edit(MethodCall m) throws CannotCompileException {
                            m.replace(body);
                        }
                    });
            ctClass.writeFile();
        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ReplaceBodyMojo replaceBodyMojo = new ReplaceBodyMojo();
        replaceBodyMojo.className = "com.alibaba.dubbo.common.bytecode.Proxy";
        replaceBodyMojo.method = "getProxy";
        replaceBodyMojo.param = "java.lang.Class[]";
        replaceBodyMojo.body = "$_ = getProxy(Thread.currentThread().getContextClassLoader(), $sig);";
        replaceBodyMojo.execute();
    }
}
