package io.moyada.sharingan.repository.mysql;


import io.moyada.sharingan.domain.expression.DataRepository;
import io.moyada.sharingan.domain.metadada.MetadataRepository;
import io.moyada.sharingan.domain.request.ReportRepository;
import io.moyada.sharingan.domain.request.ResultRepository;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
@Configuration
@MapperScan(basePackages = "io.moyada.sharingan.repository.mysql.dao")
@ConditionalOnProperty(value = "sharingan.datasource.url")
public class MybatisAutoConfiguration implements InitializingBean {

    @Autowired
    private MybatisProperties mybatisProperties;

    @Override
    public void afterPropertiesSet() {
        mybatisProperties.setMapperLocations(new String[]{"classpath*:sqlmaps/*.xml"});
    }

    @Bean
    @ConditionalOnMissingBean(MetadataRepository.class)
    public MetadataRepository getMetadataRepository() {
        return new MetadataRepositoryImpl();
    }

    @Bean
    @ConditionalOnMissingBean(DataRepository.class)
    public DataRepository getDataRepository() {
        return new DataRepositoryImpl();
    }

    @Bean({"resultRepository", "reportRepository"})
    @ConditionalOnMissingBean({ ReportRepository.class, ResultRepository.class })
    public ReportAndResultRepositoryImpl getInvocationRepository() {
        return new ReportAndResultRepositoryImpl();
    }
}
