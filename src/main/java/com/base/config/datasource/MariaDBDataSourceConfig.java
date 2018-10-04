package com.base.config.datasource;

import com.base.common.base.BaseDataSourceConfig;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
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
@PropertySource("classpath:properties/database.properties")
@EnableTransactionManagement
@MapperScan(basePackages = {"com.base.mappers.mariadb"}
        , sqlSessionFactoryRef = "mariaDBSqlSessionFactoryBean") // 다중 데이터베이스에서는 sqlSessionFactoryRef 설정이 필수다.
public class MariaDBDataSourceConfig extends BaseDataSourceConfig {

    @Value("${mysql.db.jdbc.driver}") String mysqlDriver;

    @Value("${DATABASE_JDBC_MYSQL_URL}") String mysqlUrl;
    @Value("${DATABASE_JDBC_MYSQL_USER}") String mysqlUsername;
    @Value("${DATABASE_JDBC_MYSQL_PASSWORD}") String mysqlPassword;
    @Value("${activatedProfile}") String profile;

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
        System.out.println("profile !!!!!!!!!!!!!!!!!!!!! : " + profile);
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSourceLog(mariaDBDataSource()));
        sessionFactoryBean.setConfigLocation(
                new ClassPathResource("mybatis-config.xml")
        );
        return sessionFactoryBean;
    }

}
