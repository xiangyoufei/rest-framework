package com.example.demo.config;

import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.demo.jdbc.cache.RedisCache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;

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

	/**mapper.xml文件路径，必须与其他SqlSessionFactory-mapper路径区分.*/
	@Value("${mybatis.mapperLocations}")
	public  String MAPPER_XML_PATH ;
	/**mapper.xml文件路径，必须与其他SqlSessionFactory-mapper路径区分.*/
	@Value("${mybatis.typeAliasesPackage}")
	public   String TYPE_ALIASES_PACKAGE;

	@Autowired
	protected DruidDataSource masterDataSource;

	@Autowired
	private DynamicDataSource  dynamicDataSource;

	@Autowired
	private RedisCache redisCache;
	
	@Autowired
	private PageInterceptor pageInterceptor;



	public org.apache.ibatis.session.Configuration  setConfiguration(){
		org.apache.ibatis.session.Configuration configuration=new org.apache.ibatis.session.Configuration();
		configuration.setCacheEnabled(true);
		return configuration;
	}


	//将SqlSessionFactory作为Bean注入到Spring容器中，成为配置一部分。
	@Bean(name="sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(PageInterceptor getPageInterceptor) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dynamicDataSource);
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_XML_PATH));
		sqlSessionFactoryBean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
		sqlSessionFactoryBean.setCache(redisCache);
		sqlSessionFactoryBean.setConfiguration(setConfiguration());
		sqlSessionFactoryBean.setPlugins(new Interceptor[] {getPageInterceptor});
		return sqlSessionFactoryBean.getObject();
	}
	
	@Bean
	public PageInterceptor getPageInterceptor() {
		PageInterceptor PageInterceptor = new PageInterceptor();
		Properties p = new Properties();
		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "true");
		p.setProperty("reasonable", "true");
		p.setProperty("autoRuntimeDialect ", "true");
		PageInterceptor.setProperties(p);
		return PageInterceptor;
	}

	//自定义数据库配置的时候，需要将pageHelper的bean注入到Plugins中，如果采用系统默认的数据库配置，则只需要定义pageHelper的bean，会自动注入。    
//	@Bean
//	public PageHelper pageHelper() {
//		log.info("注册MyBatis分页插件PageHelper");
//		PageHelper pageHelper = new PageHelper();
//		Properties p = new Properties();
//		p.setProperty("offsetAsPageNum", "true");
//		p.setProperty("rowBoundsWithCount", "true");
//		p.setProperty("reasonable", "true");
//		p.setProperty("autoRuntimeDialect ", "true");
//		pageHelper.setProperties(p);
//		return pageHelper;
//	}

	@Bean 
	public Interceptor getInterceptor(){
		return null; 

	}

}
