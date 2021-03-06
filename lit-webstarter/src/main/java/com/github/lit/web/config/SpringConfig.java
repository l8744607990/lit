package com.github.lit.web.config;

import com.github.lit.commons.event.guava.EventConfig;
import com.github.lit.jdbc.spring.config.EnableLitJdbc;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Arrays;
import java.util.Collections;

/**
 * User : liulu
 * Date : 2017/3/19 14:39
 * version $Id: config.java, v 0.1 Exp $
 */
@EnableLitJdbc
@Configuration
@EnableCaching
@Import(EventConfig.class)
@ComponentScan(basePackages = "com.github.lit")
public class SpringConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private C3p0ConfigProperty c3p0ConfigProperty;

    @Bean
    public DataSource dataSource() throws PropertyVetoException {

        ComboPooledDataSource source = new ComboPooledDataSource();
        source.setJdbcUrl(c3p0ConfigProperty.getJdbcUrl());
        source.setDriverClass(c3p0ConfigProperty.getDriverClass());
        source.setUser(c3p0ConfigProperty.getUser());
        source.setPassword(c3p0ConfigProperty.getPassword());
        source.setMinPoolSize(c3p0ConfigProperty.getMinPoolSize());
        source.setMaxPoolSize(c3p0ConfigProperty.getMaxPoolSize());
        source.setInitialPoolSize(c3p0ConfigProperty.getInitialPoolSize());
        source.setMaxIdleTime(c3p0ConfigProperty.getMaxIdleTime());
        source.setAutoCommitOnClose(c3p0ConfigProperty.isAutoCommitOnClose());
        source.setCheckoutTimeout(c3p0ConfigProperty.getCheckTimeOut());
        source.setAcquireRetryAttempts(2);
        source.setAutomaticTestTable(c3p0ConfigProperty.getAutomaticTestTable());
        return source;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    public SimpleCacheManager cacheManager() {

        GuavaCacheManager guavaCacheManager = new GuavaCacheManager();
        guavaCacheManager.setCacheNames(Collections.singleton("dictionary"));



        SimpleCacheManager cacheManager = new SimpleCacheManager();

        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("default"), new ConcurrentMapCache("dictionary")));

        return cacheManager;
    }




}
