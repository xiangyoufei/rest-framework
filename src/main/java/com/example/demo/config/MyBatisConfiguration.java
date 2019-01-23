package com.example.demo.config;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.demo.jdbc.cache.RedisCache;
import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * MyBatis 配置
 * 
 *@ConfigurationProperties 作用：1. 配置文件的信息，读取并自动封装成实体类 
 * 
 * 读取配置文件还可以使用{@value}
 *
 */
@Configuration
@Slf4j
public class MyBatisConfiguration {

//	private static final Logger logger = LoggerFactory.getLogger(MyBatisConfiguration.class);



	/**mapper.xml文件路径，必须与其他SqlSessionFactory-mapper路径区分.*/
	@Value("mybatis.mapperLocations")
	public  String MAPPER_XML_PATH ;
	/**mapper.xml文件路径，必须与其他SqlSessionFactory-mapper路径区分.*/
	@Value("mybatis.typeAliasesPackage")
	public   String TYPE_ALIASES_PACKAGE;


	/**使用 @ConfigurationProperties 将前缀为的spring.datasource 的几个参数自动装配为DataSourceProperties 对象 */
	@Autowired
	private DruidDataSource druidDataSource;

	@Resource
	private RedisCache redisCache;

	@Bean(name = "dynamicDataSource")
	public DynamicDataSource  DynamicDataSource() {
		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		// 默认数据源
		dynamicDataSource.setDefaultTargetDataSource(druidDataSource);
		return dynamicDataSource;
	}

	public org.apache.ibatis.session.Configuration  setConfiguration(){
		org.apache.ibatis.session.Configuration configuration=new org.apache.ibatis.session.Configuration();
		configuration.setCacheEnabled(true);
		return configuration;
	}


	//默认Bean首字母小写，简化配置 
	//将SqlSessionFactory作为Bean注入到Spring容器中，成为配置一部分。
//	@Bean(name="sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory( DynamicDataSource  dynamicDataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dynamicDataSource);
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_XML_PATH));
		sqlSessionFactoryBean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
		sqlSessionFactoryBean.setCache(redisCache);
		sqlSessionFactoryBean.setConfiguration(setConfiguration());
		return sqlSessionFactoryBean.getObject();
	}


	@Bean
	public PageHelper pageHelper() {
		log.info("注册MyBatis分页插件PageHelper");
		PageHelper pageHelper = new PageHelper();
		Properties p = new Properties();
		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "true");
		p.setProperty("reasonable", "true");
		pageHelper.setProperties(p);
		return pageHelper;
	}

	//  @Bean 
	public Interceptor getInterceptor(){
		return null; 

	}

}
