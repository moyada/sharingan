package io.moyada.spring.boot.sharingan.context;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ScanPackages {

    public static final String BEAN_NAME = "sharinganMonitorScanPackages";

    private String[] basePackages;

    public String[] getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }
}
