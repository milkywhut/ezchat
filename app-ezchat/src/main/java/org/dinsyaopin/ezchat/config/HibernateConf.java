package org.dinsyaopin.ezchat.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConf {

    @Bean
    public DatabaseConfiguration dbConfig() {
        return new DatabaseConfiguration();
    }
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource(dbConfig()));
        sessionFactory.setPackagesToScan("org.dinsyaopin.ezchat.repository");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource(DatabaseConfiguration dbConfig) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(dbConfig.getJdbcUrl());
        dataSource.setUser(dbConfig.getUsername());
        dataSource.setPassword(dbConfig.getPassword());
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager
                = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        return new Properties();
    }
}