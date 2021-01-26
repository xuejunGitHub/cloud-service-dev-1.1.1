package com.yswl.cloudservice.account.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 多数据源配置
 */
//@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "dataSource")
    @Qualifier("dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.cloudservice")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 洗衣先生数据源
     */
    @Bean(name = "washDataSource")
    @Qualifier("washDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.wash")
    public DataSource washDataSource() {
        return DataSourceBuilder.create().build();
    }


}