package io.moyada.sharingan.repository.mysql;


import io.moyada.sharingan.domain.expression.DataRepository;
import io.moyada.sharingan.domain.metadada.MetadataRepository;
import org.mybatis.spring.annotation.MapperScan;
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
public class MybatisAutoConfiguration {

    @Bean
    public MetadataRepository getMetadataRepository() {
        return new MetadataRepositoryImpl();
    }

    @Bean
    public DataRepository getDataRepository() {
        return new DataRepositoryImpl();
    }

    @Bean({"resultRepository", "reportRepository"})
    public ReportAndResultRepositoryImpl getInvocationRepository() {
        return new ReportAndResultRepositoryImpl();
    }
}
