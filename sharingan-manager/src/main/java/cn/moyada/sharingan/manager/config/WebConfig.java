/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.moyada.sharingan.manager.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author leyou
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("/public", "classpath:/META-INF/resources/");
                // .addResourceLocations("classpath:/META-INF/resources/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("redirect:/index.html");
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    @Bean
    public RouterFunction addResource() {
        return RouterFunctions.resources("/**", new ClassPathResource("classpath:/META-INF/resources/"));
    }

    @Bean
    public RouterFunction<ServerResponse> indexRouter(@Value("classpath:/META-INF/resources/index.html") final Resource indexHtml) {
        return RouterFunctions.route(RequestPredicates.GET("/"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .syncBody(indexHtml));
    }

//    @Bean
//    public FilterRegistrationBean sentinelFilterRegistration() {
//        logger.info("sentinelFilterRegistration(), add CommonFilter");
//        FilterRegistrationBean registration = new FilterRegistrationBean();
////        registration.setFilter();
//        registration.addUrlPatterns("/*");
//        registration.addInitParameter("paramName", "paramValue");
//        registration.setName("sentinelFilter");
//        registration.setOrder(1);
//        return registration;
//    }
}
