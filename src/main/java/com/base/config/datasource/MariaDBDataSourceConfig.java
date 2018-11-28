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
@MapperScan(basePackages = {"com.base.mappers.mariadb"}
        , sqlSessionFactoryRef = "mariaDBSqlSessionFactoryBean") // 다중 데이터베이스에서는 sqlSessionFactoryRef 설정이 필수다.
public class MariaDBDataSourceConfig extends BaseDataSourceConfig {

    @Value("${mysql.db.jdbc.driver}") String mysqlDriver;
    @Value("${DATABASE_JDBC_MYSQL_URL}") String mysqlUrl;
    @Value("${DATABASE_JDBC_MYSQL_USER}") String mysqlUsername;
    @Value("${DATABASE_JDBC_MYSQL_PASSWORD}") String mysqlPassword;


    @Bean
    public DataSource mariaDBDataSource(){
        return getDataSource(mysqlDriver, mysqlUrl, mysqlUsername, mysqlPassword);
    }


    @Bean
    public PlatformTransactionManager mariaDBTransactionManager(){
        return new DataSourceTransactionManager(mariaDBDataSource());
    }

    @Bean
    public SqlSessionFactoryBean mariaDBSqlSessionFactoryBean(){

        // maven 프로필 설정에따라 쿼리 로그를 남기는지 여부 결정
        DataSource dataSource = "dev".equals(profile) ? dataSourceLog(mariaDBDataSource()) : mariaDBDataSource();

        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setConfigLocation(
                new ClassPathResource("mybatis-config.xml")
        );
        return sessionFactoryBean;
    }

}
