package cn.moyada.dubbo.faker.filter.interceptor;

//import org.springframework.core.MethodParameter;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.lang.reflect.Method;
//import java.util.Map;

public class FakerInterceptor { //implements HandlerInterceptor, InitializingBean {

//    private final String applicationName;
//
//    public FakerInterceptor(String applicationName) {
//        if(null == applicationName) {
//            throw new RuntimeException("FakerInterceptor Init Error: applicationName can not be null.");
//        }
//        this.applicationName = applicationName;
//    }
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if(handler instanceof HandlerMethod) {
//            HandlerMethod handlerMethod = HandlerMethod.class.cast(handler);
//            String controllerName = handlerMethod.getBean().getClass().getName();
//            Method method = handlerMethod.getMethod();
//            method.getParameterTypes();
//            handlerMethod.getReturnType();
//            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
//        }
//
//        request.getMethod();
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        parameterMap.entrySet();
//        parameterMap.values();
//        return false;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//
//    }
}
