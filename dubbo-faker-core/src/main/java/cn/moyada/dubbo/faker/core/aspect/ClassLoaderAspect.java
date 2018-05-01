package cn.moyada.dubbo.faker.core.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author xueyikang
 * @create 2018-04-30 21:03
 */
public class ClassLoaderAspect {

    private volatile ClassLoader classLoader;

    public Object findDependency(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if(null == args || args.length == 0) {
            return joinPoint.proceed();
        }

        Object className = args[0];
        if(null == className) {
            return joinPoint.proceed();
        }

        try {
            return classLoader.loadClass(className.toString());
        }
        catch (Exception e) {
            //
        }

        return joinPoint.proceed();
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
