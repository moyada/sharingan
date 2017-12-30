package cn.xueyikang.dubbo.faker.api.aop;

import cn.xueyikang.dubbo.faker.api.annotation.Faker;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;


/**
 * Created by xueyikang on 2017/8/5.
 */
@Aspect
public class FakerAop {

    private final boolean enable;
    private final String appName;

    public FakerAop(boolean enable, String appName) {
        if(null == appName) {
            throw new RuntimeException("FakerAop Init Error: appName can not be null");
        }
        appName = appName.trim();
        if(appName.isEmpty()) {
            throw new RuntimeException("FakerAop Init Error: appName can not be null");
        }
        this.enable = enable;
        this.appName = appName;
    }

    @SuppressWarnings("unchecked")
    public void questRecord(JoinPoint jp, Faker faker) throws NoSuchMethodException {
        if(!enable) {
            return;
        }

        // 参数值
        Object[] args = jp.getArgs();

        Signature signature = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();

        // 方法名
        String methodName = signature.getName();

        // 调用接口对象
        String declaringClassName = signature.getDeclaringTypeName();

        // 返回值
        Class returnType = methodSignature.getReturnType();
        String returnTypeName = returnType.getName();
//
//        // 参数类型
        Class<?>[] parameterTypes = targetMethod.getParameterTypes();
//        String[] argsType = Stream.of(parameterTypes).map(Class::getTypeName).toArray(String[]::new);
//        String argsTypeName = JsonUtil.toJson(argsType);
//
//        CatchDTO catchDTO = CatchCache.get(declaringClassName, methodName, returnTypeName, argsTypeName);
//        if(null == catchDTO) {
//            Class[] interfaces = signature.getDeclaringType().getInterfaces();
//            if(null == interfaces || interfaces.length == 0) {
//                log.error("FakerAop Exception: {}.{}({}) can not find any interface class.", declaringClassName, declaringClassName, argsTypeName);
//                return;
//            }
//            for (Class inter : interfaces) {
//                Method method = inter.getMethod(methodName, parameterTypes);
//                if (null != method) {
//                    catchDTO = CatchCache.set(declaringClassName, methodName, returnTypeName, argsTypeName, inter.getName());
//                    break;
//                }
//            }
//        }
//
//        if(null == catchDTO) {
//            return;
//        }
//
//        catchDTO.setAppName(appName);
//        String type = faker.value();
//        catchDTO.setType(type);
//        catchDTO.setArgsValue(JsonUtil.toJson(args));

//        fakerService.catchRequest(catchDTO);
    }
}
