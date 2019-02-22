package io.moyada.sharingan.spring.boot.autoconfigure;

import io.moyada.sharingan.spring.boot.autoconfigure.annotation.EnableMonitor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MonitorRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        String[] basePackages = getBasePackages(annotationMetadata);
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ScanPackages.class);
        builder.addPropertyValue("basePackages", basePackages);
        beanDefinitionRegistry.registerBeanDefinition("monitorScanPackages", builder.getBeanDefinition());
    }

    public String[] getBasePackages(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableMonitor.class.getName()));
        if (null == attributes) {
            return new String[0];
        }

        Set<String> packages = new HashSet<>();

        String[] values = attributes.getStringArray("value");
        for (String value : values) {
            add(packages, value);
        }

        String[] basePackages = attributes.getStringArray("basePackages");
        for (String basePackage : basePackages) {
            add(packages, basePackage);
        }

        Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
        for (Class<?> basePackageClass : basePackageClasses) {
            add(packages, ClassUtils.getPackageName(basePackageClass));
        }

        if (packages.isEmpty()) {
            return new String[0];
        }
        return packages.toArray(new String[0]);
    }

    private void add(Set<String> source, String item) {
        for (String s : source) {
            if (item.startsWith(s)) {
                return;
            }
        }
        source.add(item);
    }
}
