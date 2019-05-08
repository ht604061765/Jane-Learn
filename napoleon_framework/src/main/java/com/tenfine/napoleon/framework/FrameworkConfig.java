package com.tenfine.napoleon.framework;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;

import java.sql.SQLException;

/**
 * 数据库相关配置
 * @author Hunter
 */
@Configuration
public class FrameworkConfig {

    /**
     * 数据库驱动
     */
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * 数据库连接 url
     */
    @Value("${spring.datasource.url}")
    private String dbUrl;

    /**
     * 数据库用户
     */
    @Value("${spring.datasource.username}")
    private String username;

    /**
     * 数据库密码
     */
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * 相当于在web.xml增加servlet
     * @return ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servletRegBean = new ServletRegistrationBean();
        servletRegBean.setServlet(new StatViewServlet());
        servletRegBean.addUrlMappings("/druid/*");
        servletRegBean.addInitParameter("loginUsername", "admin");
        servletRegBean.addInitParameter("loginPassword", "admin");
        // 监控信息属于敏感信息，如有必要要限制，如：128.242.127.1/24,128.242.128.1
        servletRegBean.addInitParameter("allow", "");
        servletRegBean.addInitParameter("deny", "");
        return servletRegBean;
    }

    /**
     * 相当于在web.xml增加filter
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        filterRegBean.setFilter(new WebStatFilter());
        filterRegBean.addUrlPatterns("/*");
        filterRegBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,/bud/*");
        filterRegBean.addInitParameter("profileEnable", "true");
        filterRegBean.addInitParameter("sessionStatMaxCount", "1000");
        filterRegBean.addInitParameter("sessionStatEnable", "true");
        return filterRegBean;
    }

    /**
     * 数据源
     *
     * @return DataSource
     */
    @Bean
    @Primary
    public DataSource druidDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        // DruidDataSource
        datasource.setDriverClassName(driverClassName);
        datasource.setUrl(dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);

        // DruidDataSource属性
        datasource.setInitialSize(5);
        datasource.setMinIdle(5);
        datasource.setMaxActive(200);
        datasource.setMaxWait(60000);
        datasource.setTimeBetweenEvictionRunsMillis(60000);
        datasource.setMinEvictableIdleTimeMillis(300000);
        datasource.setValidationQuery("SELECT 1 FROM DUAL");
        datasource.setTestWhileIdle(true);
        datasource.setTestOnBorrow(false);
        datasource.setTestOnReturn(false);
        datasource.setPoolPreparedStatements(true);
        datasource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");
        try {
            datasource.setFilters("stat,config");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasource;
    }

    /**
     * Druid和Spring关联监控配置
     * @return DruidStatInterceptor
     */
    @Bean
    public DruidStatInterceptor druidStatInterceptor() {
        return new DruidStatInterceptor();
    }

    /**
     * 配置Spring Jdbc模板 jdbcTemplate:
     * @return NamedParameterJdbcTemplate
     */
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(druidDataSource());
    }

}
