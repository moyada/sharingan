package cn.moyada.sharingan.storage.mysql;

import cn.moyada.sharingan.storage.api.ArgsRepository;
import cn.moyada.sharingan.storage.api.InvocationRepository;
import cn.moyada.sharingan.storage.api.MetadataRepository;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
@Configuration
@MapperScan(basePackages = "cn.moyada.sharingan.storage.mysql.dao")
@ConditionalOnProperty(value = "spring.datasource.url", matchIfMissing = true)
public class MybatisAutoConfiguration {

    @Bean
    public ArgsRepository getArgsRepository() {
        return new ArgsRepositoryImpl();
    }

    @Bean
    public InvocationRepository getInvocationRepository() {
        return new InvocationRepositoryImpl();
    }

    @Bean
    public MetadataRepository getMetadataRepository() {
        return new MetadataRepositoryImpl();
    }
}
