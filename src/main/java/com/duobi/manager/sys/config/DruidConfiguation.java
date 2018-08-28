package com.duobi.manager.sys.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;


/**
 * 数据库连接池&Mybatis配置类
 *
 */
@Configuration
//@EnableTransactionManagement
@MapperScan(basePackages = "com.duobi.manager.sys.dao", sqlSessionTemplateRef  = "serviceSqlSessionTemplate")
public class DruidConfiguation {

    private static String MYBATIS_CONFIG = "mybatis/mybatis-config.xml";

    //配置第一数据源（默认数据源）
    @Bean(name = "serviceDataSource")
    @Qualifier("serviceDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

    @Bean(name = "serviceTransactionManager")
    @Primary
    public PlatformTransactionManager setTransactionManager(@Qualifier("serviceDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "serviceSqlSessionFactory")
    @Primary
    public SqlSessionFactory setSqlSessionFactory(@Qualifier("serviceDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        /** 设置mybatis configuration 扫描路径 */
        bean.setConfigLocation(new ClassPathResource(MYBATIS_CONFIG));
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/sys/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "serviceSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate setSqlSessionTemplate(@Qualifier("serviceSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

//    @Bean("servicePlatformTransactionManager")
//    @Override
//    public PlatformTransactionManager annotationDrivenTransactionManager() {
//        return new DataSourceTransactionManager(druidDataSource());
//    }
}