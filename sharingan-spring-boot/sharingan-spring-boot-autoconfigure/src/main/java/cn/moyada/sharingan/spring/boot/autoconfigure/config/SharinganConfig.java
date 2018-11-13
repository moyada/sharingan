package cn.moyada.sharingan.spring.boot.autoconfigure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
@ConfigurationProperties(prefix = SharinganProperties.PREFIX_NAME)
public class SharinganConfig {// implements EnvironmentAware {

    private boolean enable = false;

    private String application;

    private Map<String, String> attach;

    public SharinganConfig() {
        System.out.println("init");
    }

    //    @Override
//    public void setEnvironment(Environment environment) {
//        Boolean enable = environment.getProperty(SharinganProperties.ENABLE, Boolean.class);
//        if (null != enable) {
//            this.enable = enable;
//        }
//
//        String application = environment.getProperty(SharinganProperties.PREFIX_NAME + ".application", String.class);
//        if (null != application) {
//            this.application = application;
//        }
//    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public Map<String, String> getAttach() {
        return attach;
    }

    public void setAttach(Map<String, String> attach) {
        this.attach = attach;
    }
}
