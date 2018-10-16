package com.github.mhdirkse.timewriter.db;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "stubEntityManagerFactory",
        transactionManagerRef = "stubTransactionManager",
        basePackages = {"com.github.mhdirkse.timewriter"})
class StubDbConfig {
    @Bean(name = "stubDataSource")
    @ConfigurationProperties(prefix = "stub.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "stubEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    stubEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("stubDataSource") DataSource dataSource) {
        return
          builder
            .dataSource(dataSource)
            .packages("com.github.mhdirkse.timewriter.model")
            .persistenceUnit("stub")
            .build();
    }

    @Bean(name = "stubTransactionManager")
    public PlatformTransactionManager stubTransactionManager(
            @Qualifier("stubEntityManagerFactory") EntityManagerFactory stubEntityManagerFactory)
    {
        return new JpaTransactionManager(stubEntityManagerFactory);
    }

    @Bean
    public SecurityRefinerForH2 securityRefinerForH2() {
        return new SecurityRefinerForH2Impl();
    }
}