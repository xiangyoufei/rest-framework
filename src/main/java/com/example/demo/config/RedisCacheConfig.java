package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableCaching//启用缓存
@EnableConfigurationProperties(RedisProperties.class)
@ConfigurationProperties(prefix = "spring.redis.pool")
public class RedisCacheConfig extends CachingConfigurerSupport{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());  


	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.password}")
	private String password;

	@Value("${spring.redis.timeout}")
	private int timeout;

	@Value("${spring.redis.pool.maxIdle}")
	private int maxIdle;

	@Value("${spring.redis.pool.maxWait}")
	private int maxWaitMillis;

	@Value("${spring.redis.pool.maxActive}")
	private int maxTotal;

	@Value("${spring.redis.pool.nodes}")
	private String nodes; 


	@Bean
	public JedisPoolConfig jedisPoolConfig () {  
		JedisPoolConfig config = new JedisPoolConfig();  
		config.setMaxTotal(maxTotal);  
		config.setMaxIdle(maxIdle);  
		config.setMaxWaitMillis(maxWaitMillis);  
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		return config;  
	}  

	
	/**
	 * 单节点的redis连接池
	 * @param jedisPoolConfig
	 * @return
	 */
	@Bean
	public JedisPool redisPoolFactory(JedisPoolConfig jedisPoolConfig) {
		logger.info(" redispool 使用config 注解");
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
		return jedisPool;
	}
	
	/**
	 * redis使用方法之一 ：Jedis
	 * @param jedisPool
	 * @return
	 */
//	@Bean
//	public Jedis getJedis(JedisPool jedisPool) {
//		return jedisPool.getResource();
//	}

	/**
	 * 单节点的reids密码配置
	 * @return
	 */
//	/*@Bean
//	public RedisStandaloneConfiguration standaloneConfig() {
//		RedisStandaloneConfiguration config=new RedisStandaloneConfiguration();
//		config.setHostName(host);
//		config.setPassword(RedisPassword.of(password.toCharArray()));
//		config.setPort(port);
//		return config;
//	}*/
	

	/**
	 * redis连接工厂(单节点配置)
	 * 没有配置连接池信息
	 * @param jedisPoolConfig
	 * @return
	 */
	@Bean
	@Primary
	public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
		JedisConnectionFactory factory=new JedisConnectionFactory(jedisPoolConfig);
		return factory;
	}
	/************************************************以上是单机节点配置************************************************/
	
	@Bean
	public RedisClusterConfiguration redisClusterConfiguration() {
		RedisClusterConfiguration clusterConfig=new RedisClusterConfiguration();
		clusterConfig.setPassword(RedisPassword.of(password.toCharArray()));
		clusterConfig.setMaxRedirects(5000);
		String[] serverArray = nodes.split(",");//获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)  
		Set<RedisNode> nodes = new HashSet<>();  
		for (String ipPort : serverArray) {  
			String[] ipPortPair = ipPort.split(":");  
			nodes.add(new RedisNode(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));  
		}  
		clusterConfig.setClusterNodes(nodes);
		return clusterConfig;
	}
	
	/** redis连接工厂(集群配置)
	 * @param redisClusterConfiguration
	 * @return
	 */
	@Bean
	public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig,RedisClusterConfiguration redisClusterConfiguration) {
		JedisConnectionFactory factory=new JedisConnectionFactory(redisClusterConfiguration,jedisPoolConfig);
		return factory;
	}


	/**
	 * reids 使用方法之二: redisTemplate
	 * @param factory
	 * @return
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory factory){
		RedisTemplate<String, Object> template=new RedisTemplate<>();
		template.setConnectionFactory(factory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());
		return template;
	}

	/**
	 *  redis 使用方法之三: JedisCluster
	 * @return
	 */
	@Bean  
	public JedisCluster getJedisCluster() {  
		String[] serverArray = nodes.split(",");//获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)  
		Set<HostAndPort> nodes = new HashSet<>();  

		for (String ipPort : serverArray) {  
			String[] ipPortPair = ipPort.split(":");  
			nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));  
		}  

		return new JedisCluster(nodes,timeout);  
	} 




}

