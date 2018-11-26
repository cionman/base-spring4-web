package com.base.common.base;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import net.sf.log4jdbc.tools.Log4JdbcCustomFormatter;
import net.sf.log4jdbc.tools.LoggingType;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


/**
 * DatasourceConfig Base
 */
public class BaseDataSourceConfig {

    @Value("${db.maxTotal}") int maxTotal;
    @Value("${db.maxIdle}") int maxIdle;
    @Value("${db.minIdle}") int minIdle;
    @Value("${db.maxWaitMillis}") long maxWaitMillis;
    @Value("${activatedProfile}") protected String profile;

    protected DataSource dataSourceLog(DataSource dataSource){
        Log4jdbcProxyDataSource logDataSource =new Log4jdbcProxyDataSource(dataSource);
        Log4JdbcCustomFormatter formatter = new Log4JdbcCustomFormatter();
        formatter.setLoggingType(LoggingType.MULTI_LINE);
        formatter.setSqlPrefix("SQL         :  ");
        logDataSource.setLogFormatter(formatter);

        return logDataSource;
    }


    protected DataSource getDataSource(
            String driver
            , String url
            , String username
            , String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDefaultAutoCommit(true);
        dataSource.setMaxTotal(maxTotal);
        dataSource.setMaxIdle(maxIdle);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxWaitMillis(maxWaitMillis);
        return dataSource;
    }

}
