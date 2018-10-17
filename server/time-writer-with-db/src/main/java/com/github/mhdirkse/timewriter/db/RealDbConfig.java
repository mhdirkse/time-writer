package com.github.mhdirkse.timewriter.db;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "realEntityManagerFactory",
        transactionManagerRef = "realTransactionManager",
        basePackages = {"com.github.mhdirkse.timewriter"})
class RealDbConfig {
    @Bean(name = "realDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "realEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean
    realEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("realDataSource") DataSource dataSource) {
        return
          builder
            .dataSource(dataSource)
            .packages("com.github.mhdirkse.timewriter.model")
            .persistenceUnit("real")
            .build();
    }

    @Bean(name = "realTransactionManager")
    @Primary
    public PlatformTransactionManager realTransactionManager(
            @Qualifier("realEntityManagerFactory") EntityManagerFactory realEntityManagerFactory)
    {
        return new JpaTransactionManager(realEntityManagerFactory);
    }

    @Bean
    public SecurityRefinerForH2 securityRefinerForH2Noop() {
        return new SecurityRefinerForH2Noop();
    }
}