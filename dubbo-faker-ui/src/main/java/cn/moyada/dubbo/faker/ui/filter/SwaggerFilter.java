package cn.moyada.dubbo.faker.ui.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

public class SwaggerFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.printf("SwaggerFilter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest hrequest = (HttpServletRequest) servletRequest;
        String requestURI = hrequest.getRequestURI();
        if("/api-docs".equals(requestURI)) {
//            RequestDispatcher dispatcher = servletRequest.getRequestDispatcher("/swagger-ui.html");
//            dispatcher.forward(servletRequest, servletResponse);
            HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) servletResponse);
            wrapper.sendRedirect("/swagger-ui.html");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
