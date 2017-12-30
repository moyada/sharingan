package cn.xueyikang.dubbo.faker.ui.config;

/**
 * Created by xueyikang on 2017/12/22.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;


//@Configuration
//@EnableWebSecurity
@EnableWebMvc
@EnableSwagger2
public class Swagger2Config implements WebMvcConfigurer {

    @Bean
    public SecurityScheme apiKey() {
        return new ApiKey("access_token", "accessToken", "header");
    }

    private static final String[] AUTH_WHITELIST = {

            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/api-docs",
            "/webjars/**"
    };


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .produces(produecetypes())
//                .groupName("demo-api")
//                .apiInfo(demoapiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
//                .ignoredParameterTypes(ApiIgnore.class);
//                .enableUrlTemplating(false);
//                .securitySchemes(Collections.singletonList(apiKey()));
    }

    private ApiInfo demoapiInfo() {
        return new ApiInfoBuilder()
                .title("Demo swagger descripted API")
                .description("这是一个Swagger描述的demo.")
                .version("2.0")
                .build();
    }

    private HashSet<String> produecetypes(){
        HashSet <String> hs = new HashSet<String>();
        hs.add(MediaType.APPLICATION_JSON_VALUE);
        hs.add(MediaType.TEXT_HTML_VALUE);
        return hs;
    }

    /**
     * 配置servlet处理
     */
//     @Override
//     public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//         configurer.enable();
//     }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");

//        registry.addResourceHandler("swagger-ui.html")
////                .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
//                .addResourceLocations("classpath:/META-INF/resources/");
//
//        registry.addResourceHandler("webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");


        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/api-docs", "/swagger-ui.html");
        registry.addRedirectViewController("/docApi/v2/api-docs", "/v2/api-docs");
        registry.addRedirectViewController("/docApi/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
        registry.addRedirectViewController("/docApi/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
        registry.addRedirectViewController("/docApi/swagger-resources", "/swagger-resources");
    }

    @Bean
    public Set<Class> defaultIgnorableParameterTypes() {
        HashSet<Class> ignored = newHashSet();
        ignored.add(ServletRequest.class);
        ignored.add(Class.class);
        ignored.add(Void.class);
        ignored.add(Void.TYPE);
        ignored.add(ServletResponse.class);
        ignored.add(HttpServletRequest.class);
        ignored.add(HttpServletResponse.class);
        ignored.add(HttpHeaders.class);
        ignored.add(BindingResult.class);
        ignored.add(ServletContext.class);
        ignored.add(UriComponentsBuilder.class);
        ignored.add(ApiIgnore.class);
        return ignored;
    }

    /**
     * Default response messages set on all api operations
     */
    @Bean
    public Map<RequestMethod, List<ResponseMessage>> defaultResponseMessages() {
        LinkedHashMap<RequestMethod, List<ResponseMessage>> responses = newLinkedHashMap();
        responses.put(GET, asList(
                new ResponseMessageBuilder()
                        .code(OK.value())
                        .message(OK.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(NOT_FOUND.value())
                        .message(NOT_FOUND.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(FORBIDDEN.value())
                        .message(FORBIDDEN.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(UNAUTHORIZED.value())
                        .message(UNAUTHORIZED.getReasonPhrase())
                        .responseModel(null).build()));

        responses.put(PUT, asList(
                new ResponseMessageBuilder()
                        .code(CREATED.value())
                        .message(CREATED.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(NOT_FOUND.value())
                        .message(NOT_FOUND.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(FORBIDDEN.value())
                        .message(FORBIDDEN.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(UNAUTHORIZED.value())
                        .message(UNAUTHORIZED.getReasonPhrase())
                        .responseModel(null).build()));

        responses.put(POST, asList(
                new ResponseMessageBuilder()
                        .code(CREATED.value())
                        .message(CREATED.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(NOT_FOUND.value())
                        .message(NOT_FOUND.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(FORBIDDEN.value())
                        .message(FORBIDDEN.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(UNAUTHORIZED.value())
                        .message(UNAUTHORIZED.getReasonPhrase())
                        .responseModel(null).build()));

        responses.put(DELETE, asList(
                new ResponseMessageBuilder()
                        .code(NO_CONTENT.value())
                        .message(NO_CONTENT.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(FORBIDDEN.value())
                        .message(FORBIDDEN.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(UNAUTHORIZED.value())
                        .message(UNAUTHORIZED.getReasonPhrase())
                        .responseModel(null)
                        .build()));

        responses.put(PATCH, asList(
                new ResponseMessageBuilder()
                        .code(NO_CONTENT.value())
                        .message(NO_CONTENT.getReasonPhrase())
                        .responseModel(null).build(),
                new ResponseMessageBuilder()
                        .code(FORBIDDEN.value())
                        .message(FORBIDDEN.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(UNAUTHORIZED.value())
                        .message(UNAUTHORIZED.getReasonPhrase())
                        .responseModel(null)
                        .build()));

        responses.put(TRACE, asList(
                new ResponseMessageBuilder()
                        .code(NO_CONTENT.value())
                        .message(NO_CONTENT.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(FORBIDDEN.value())
                        .message(FORBIDDEN.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(UNAUTHORIZED.value())
                        .message(UNAUTHORIZED.getReasonPhrase())
                        .responseModel(null)
                        .build()));

        responses.put(OPTIONS, asList(
                new ResponseMessageBuilder()
                        .code(NO_CONTENT.value())
                        .message(NO_CONTENT.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(FORBIDDEN.value())
                        .message(FORBIDDEN.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(UNAUTHORIZED.value())
                        .message(UNAUTHORIZED.getReasonPhrase())
                        .responseModel(null)
                        .build()));
        responses.put(HEAD, asList(
                new ResponseMessageBuilder()
                        .code(NO_CONTENT.value())
                        .message(NO_CONTENT.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(FORBIDDEN.value())
                        .message(FORBIDDEN.getReasonPhrase())
                        .responseModel(null)
                        .build(),
                new ResponseMessageBuilder()
                        .code(UNAUTHORIZED.value())
                        .message(UNAUTHORIZED.getReasonPhrase())
                        .responseModel(null)
                        .build()));
        return responses;
    }
}