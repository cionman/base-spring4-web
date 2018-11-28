package com.base.config.datasource;

import com.base.common.base.BaseDataSourceConfig;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:properties/database.properties")
@MapperScan(basePackages = {"com.base.mappers.sqlserver"}
            , sqlSessionFactoryRef = "sqlServerSqlSessionFactoryBean")
public class SqlServerDataSourceConfig extends BaseDataSourceConfig {

    @Value("${sqlserver.db.jdbc.driver}") String sqlServerDriver;

    @Value("${DATABASE_JDBC_SQLSERVER_URL}") String sqlserverUrl;
    @Value("${DATABASE_JDBC_SQLSERVER_USER}") String sqlserverUsername;
    @Value("${DATABASE_JDBC_SQLSERVER_PASSWORD}") String sqlserverPassword;

    @Bean
    public DataSource sqlServerDataSource(){
        return getDataSource(sqlServerDriver, sqlserverUrl, sqlserverUsername, sqlserverPassword);
    }

    @Bean
    public PlatformTransactionManager sqlserverTransactionManager(){
        return new DataSourceTransactionManager(sqlServerDataSource());
    }

    @Bean
    public SqlSessionFactoryBean sqlServerSqlSessionFactoryBean(){

        DataSource dataSource = "dev".equals(profile) ? dataSourceLog(sqlServerDataSource()) : sqlServerDataSource();

        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSourceLog(sqlServerDataSource()));
        sessionFactoryBean.setConfigLocation(
                new ClassPathResource("mybatis-config.xml")
        );
        return sessionFactoryBean;
    }


}
