package cn.moyada.sharingan.instrument.boost.proxy;

//import com.squareup.javapoet.ClassName;
//import com.squareup.javapoet.JavaFile;
//import com.squareup.javapoet.TypeSpec;
import cn.moyada.sharingan.instrument.boost.NameUtil;
import cn.moyada.sharingan.instrument.boost.common.ProxyMethod;
import javassist.NotFoundException;

import javax.annotation.processing.Filer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class JavaPoetProxy implements ClassProxy {

    private final String LOCAL_VARIABLE = "_invoke";

    private final Class invokeParam;

    private final String invokeClassName;

    private final String invokeObjName;

    private final String proxyMethod;

    private final Map<String, String> privateVariables;

    private final StringBuilder invokeBody;

    private final Filer filer;

    public JavaPoetProxy(Class invokeClass, Method invokeMethod,
                         Class invokeParam, String[] privateVariables, Filer filer) throws NotFoundException {

        this.invokeClassName = invokeClass.getName();
        this.invokeObjName = NameUtil.genPrivateName(invokeClass.getSimpleName());
        this.proxyMethod = invokeMethod.getName();

        this.invokeParam = invokeParam;

        this.privateVariables = new HashMap<>();
        for (String privateVariable : privateVariables) {
            this.privateVariables.put(NameUtil.genPrivateName(privateVariable), NameUtil.getSetFunction(privateVariable));
        }

        this.filer = filer;

        this.invokeBody = new StringBuilder(128);
    }

    @Override
    public <T> Class<T> wrapper(Class<T> target, List<ProxyMethod> methods) throws Exception {
//        ClassName className = ClassName.get(target);
//
//        TypeSpec.Builder builder = TypeSpec.classBuilder(className);
//
//        JavaFile javaFile = JavaFile.builder(target.getPackage().getName(), builder.build()).build();
//        javaFile.writeTo(filer);

        return target;
    }
}
