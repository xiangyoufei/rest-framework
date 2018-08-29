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

/**
 * MyBatis 配置
 * 
 *@ConfigurationProperties 作用：1. 配置文件的信息，读取并自动封装成实体类 
 * 
 * 读取配置文件还可以使用{@value}
 *
 */
@Configuration
public class MyBatisConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(MyBatisConfiguration.class);



	/**SqlSessionFactory名称.*/
	public final static String SESSIONFACTORY_NAME = "sqlSessionFactory";
	/**mapper包路径，必须与其他SqlSessionFactory-mapper路径区分.*/
	public final static String MAPPER_PACKAGE = "com.example.demo.dao";
	/**mapper.xml文件路径，必须与其他SqlSessionFactory-mapper路径区分.*/
	public final static String MAPPER_XML_PATH = "classpath:mapper/*.xml";
	/**mapper.xml文件路径，必须与其他SqlSessionFactory-mapper路径区分.*/
	public final static String TYPE_ALIASES_PACKAGE = "com.example.demo.entity";


	/**使用 @ConfigurationProperties 将前缀为的spring.datasource 的几个参数自动装配为DataSourceProperties 对象 */
	@Autowired
	private DataSourceProperties dataSourceProperties;

	@Resource
	private RedisCache redisCache;



	@Value("${spring.datasource.max-wait}")
	private int maxWait;

	@Value("${spring.datasource.max-idle}")
	private int maxIdle;

	@Value("${spring.datasource.min-idle}")
	private int minIdle;

	@Value("${spring.datasource.initial-size}")
	private int initialSize;

	@Bean(name = "dataSource")
	public DataSource dataSource() {
		//建议封装成单独的类
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(dataSourceProperties.getUrl());
		System.err.println(dataSourceProperties.getUrl());
		dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
		dataSource.setUsername(dataSourceProperties.getUsername());
		dataSource.setPassword(dataSourceProperties.getPassword());
		dataSource.setInitialSize(initialSize);
		dataSource.setMinIdle(minIdle);
		//      dataSource.setMaxIdle(maxIdle);
		dataSource.setMaxWait(maxWait);
		return dataSource;
	}

	@Bean(name = "dynamicDataSource")
	public DataSource DynamicDataSource(@Qualifier("dataSource") DataSource dataSource) {
		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		// 默认数据源
		dynamicDataSource.setDefaultTargetDataSource(dataSource);


		return dynamicDataSource;
	}

	public org.apache.ibatis.session.Configuration  setConfiguration(){
		org.apache.ibatis.session.Configuration configuration=new org.apache.ibatis.session.Configuration();
		configuration.setCacheEnabled(true);
		return configuration;
	}


	//默认Bean首字母小写，简化配置 
	//将SqlSessionFactory作为Bean注入到Spring容器中，成为配置一部分。
	@Bean(SESSIONFACTORY_NAME)
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_XML_PATH));
		sqlSessionFactoryBean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
		sqlSessionFactoryBean.setCache(redisCache);
		sqlSessionFactoryBean.setConfiguration(setConfiguration());
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

	//  @Bean 
	public Interceptor getInterceptor(){
		return null; 

	}

}
