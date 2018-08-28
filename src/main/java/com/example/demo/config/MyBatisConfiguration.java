package com.example.demo.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
 
/**
 * MyBatis 配置
 *
 */
@Configuration
public class MyBatisConfiguration {
 
  private static final Logger logger = LoggerFactory.getLogger(MyBatisConfiguration.class);
 
 
  
  /**SqlSessionFactory名称.*/
  public final static String SESSIONFACTORY_NAME = "sqlSessionFactory";
  /**mapper包路径，必须与其他SqlSessionFactory-mapper路径区分.*/
  public final static String MAPPER_PACKAGE = "com.example.datas.manager.mapper";
  /**mapper.xml文件路径，必须与其他SqlSessionFactory-mapper路径区分.*/
  public final static String MAPPER_XML_PATH = "classpath:mapper/*.xml";

  @Autowired
  private DataSourceProperties dataSourceProperties;


  @Bean(name = "dataSource")
  public DataSource dataSource() {
      //建议封装成单独的类
      DruidDataSource dataSource = new DruidDataSource();
      dataSource.setUrl(dataSourceProperties.getUrl());
      System.err.println(dataSourceProperties.getUrl());
      dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
      dataSource.setUsername(dataSourceProperties.getUsername());
      dataSource.setPassword(dataSourceProperties.getPassword());
      return dataSource;
  }
  

  //默认Bean首字母小写，简化配置 
  //将SqlSessionFactory作为Bean注入到Spring容器中，成为配置一部分。
  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {
      SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
      sqlSessionFactoryBean.setDataSource(dataSource());
      sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_XML_PATH));
      return sqlSessionFactoryBean.getObject();
  }
  
  
  @Bean
  public PageHelper pageHelper() {
    logger.info("注册MyBatis分页插件PageHelper");
    PageHelper pageHelper = new PageHelper();
    Properties p = new Properties();
    p.setProperty("offsetAsPageNum", "true");
    p.setProperty("rowBoundsWithCount", "true");
    p.setProperty("reasonable", "true");
    pageHelper.setProperties(p);
    return pageHelper;
  }
 
}
