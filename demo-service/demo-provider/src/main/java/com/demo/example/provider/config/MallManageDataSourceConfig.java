package com.demo.example.provider.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author liull
 * @description
 * @date 2021/6/3
 */
@Configuration
@Data
@MapperScan(basePackages = MallManageDataSourceConfig.PACKAGE,sqlSessionFactoryRef = "mallManageSqlSessionFactory")
public class MallManageDataSourceConfig {

    /**
     * dao层的包路径
     */
    static final String PACKAGE = "com.demo.example.provider.dao";

    /**
     * mapper文件的相对路径
     */
    private static final String MAPPER_LOCATION = "classpath:mapper/*.xml";

    @Bean("mallManageDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mall-manage.druid")
    public DataSource mallManageDataSource(){
        return new DruidDataSource();
    }

    @Bean("mallManageDataSourceTransactionManager")
    public DataSourceTransactionManager mallManageDataSourceTransactionManager(@Qualifier("mallManageDataSource") DataSource mallManageDataSource){
        return new DataSourceTransactionManager(mallManageDataSource);
    }

    @Bean("mallManageSqlSessionFactory")
    public SqlSessionFactory mallManageSqlSessionFactory(@Qualifier("mallManageDataSource") DataSource mallManageDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(mallManageDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MallManageDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }

}
