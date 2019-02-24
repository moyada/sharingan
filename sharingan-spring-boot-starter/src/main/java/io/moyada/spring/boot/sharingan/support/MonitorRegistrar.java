package io.moyada.spring.boot.sharingan.support;

import io.moyada.spring.boot.sharingan.annotation.EnableSharinganMonitor;
import io.moyada.spring.boot.sharingan.annotation.EnableSharinganMonitorScan;
import io.moyada.spring.boot.sharingan.context.ScanPackages;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnBean(annotation = EnableSharinganMonitorScan.class)
@ConditionalOnProperty(name = EnableSharinganMonitor.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
public class MonitorRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        String[] basePackages = getBasePackages(annotationMetadata);
        if (basePackages == null) {
            return;
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ScanPackages.class);
        builder.setRole(BeanDefinition.ROLE_APPLICATION);
        builder.addPropertyValue("basePackages", basePackages);
        beanDefinitionRegistry.registerBeanDefinition("monitorScanPackages", builder.getBeanDefinition());
    }

    private String[] getBasePackages(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableSharinganMonitor.class.getName()));
        if (null == attributes) {
            return null;
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
            add(packages, ClassUtils.getPackageName(metadata.getClassName()));
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





