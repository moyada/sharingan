package cn.moyada.dubbo.faker.ui.config;

/**
 * Created by xueyikang on 2017/12/22.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;

import java.util.HashSet;


//@Configuration
//@EnableWebMvc
//@EnableSwagger
public class SwaggerConfig implements WebMvcConfigurer {

//    @Autowired
//    private SpringSwaggerConfig springSwaggerConfig;

    /**
     * Required to autowire SpringSwaggerConfig
     */
//    @Autowired
//    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig)
//    {
//        this.springSwaggerConfig = springSwaggerConfig;
//    }

//    @Bean //Don't forget the @Bean annotation
//    public SwaggerSpringMvcPlugin customImplementation(){
//        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
//                .apiInfo(apiInfo())
//                .includePatterns(".*pet.*");
//    }

//    @Bean
//    public SwaggerSpringMvcPlugin customImplementation()
//    {
//        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
//                .apiInfo(apiInfo())
//                .includePatterns(".*?");
//    }

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

//    private ApiInfo apiInfo() {
//        ApiInfo apiInfo = new ApiInfo(
//                "Dubbo Faker swagger",
//                "API swagger",
//                "http://localhost:8080/api-docs",
//                null,
//                "web app",
//                "My Apps API License URL");
//        return apiInfo;
//    }

//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
////                .produces(produecetypes())
////                .groupName("demo-api")
////                .apiInfo(demoapiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build()
//                .ignoredParameterTypes(ApiIgnore.class);
////                .enableUrlTemplating(false);
////                .securitySchemes(Collections.singletonList(apiKey()));
//    }

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

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/docApi/v2/api-docs", "/v2/api-docs");
        registry.addRedirectViewController("/docApi/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
        registry.addRedirectViewController("/docApi/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
        registry.addRedirectViewController("/docApi/swagger-resources", "/swagger-resources");
    }
}