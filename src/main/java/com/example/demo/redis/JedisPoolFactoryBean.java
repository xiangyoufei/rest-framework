package com.example.demo.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * JedisPool 工厂
 * Spring中有两种类型的Bean，一种是普通Bean，另一种是工厂Bean，即FactoryBean。
 * 工厂Bean跟普通Bean不同，其返回的对象不是指定类的一个实例，其返回的是该工厂Bean的getObject方法所返回的对象。
 * 			创建出来的对象是否属于单例由isSingleton中的返回决定。
 * 使用场景：1、通过外部对类是否是单例进行控制，该类自己无法感知 2、对类的创建之前进行初始化的操作，
 * @since 1.0.0
 */
/**
 * Spring初始化bean的时候，如果bean实现了InitializingBean接口，会自动调用afterPropertiesSet方法。
 * 	并且同时在配置文件中指定了init-method，系统则是先调用afterPropertiesSet方法，然后在调用init-method中指定的方法。
 * 	也就是说这个方法在spring启动时候优先调用
 *  当然为了初始化某个方法，也可以使用init-method在配置文件设置，但是启动顺序 InitializingBean 更早
 */
@Component
public class JedisPoolFactoryBean implements FactoryBean<JedisPool>, InitializingBean {
	
	private static final Logger logger=LoggerFactory.getLogger(JedisPoolFactoryBean.class);

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 6379;
    private static final int DEFAULT_TIMEOUT = 2000;
    private static final int DEFAULT_MAX_TOTAL = 100;
    private static final int DEFAULT_MAX_IDLE = 20;
    private static final int DEFAULT_MIN_IDLE = 5;
    private static final boolean DEFAULT_TEST_ON_BORROW = true;
    private static final boolean DEFAULT_TEST_ON_RETURN = true;

    private JedisPool pool;

/*    private String host = DEFAULT_HOST;
    private int port = DEFAULT_PORT;
    private int timeout = DEFAULT_TIMEOUT;
    private String password;
    private int maxTotal = DEFAULT_MAX_TOTAL;
    private int maxIdle = DEFAULT_MAX_IDLE;
    private int minIdle = DEFAULT_MIN_IDLE;
    private boolean testOnBorrow = DEFAULT_TEST_ON_BORROW;
    private boolean testOnReturn = DEFAULT_TEST_ON_RETURN;*/
    
	@Value("${spring.redis.host}")
    private String host = DEFAULT_HOST;
	@Value("${spring.redis.port}")
    private int port = DEFAULT_PORT;
	@Value("${spring.redis.timeout}")
    private int timeout = DEFAULT_TIMEOUT;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.pool.max-active}")
    private int maxTotal = DEFAULT_MAX_TOTAL;
	@Value("${spring.redis.pool.max-idle}")
    private int maxIdle = DEFAULT_MAX_IDLE;
	@Value("${spring.redis.pool.min-idle}")
    private int minIdle = DEFAULT_MIN_IDLE;
    private boolean testOnBorrow = DEFAULT_TEST_ON_BORROW;
    private boolean testOnReturn = DEFAULT_TEST_ON_RETURN;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    @Override
    public JedisPool getObject() throws Exception {
        return pool;
    }

    @Override
    public Class<?> getObjectType() {
        return JedisPool.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

   
    @Override
    public void afterPropertiesSet() throws Exception {
        JedisPoolConfig config =new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        logger.info("  redispool   使用 InitializingBean 初始化");
        pool = new JedisPool(config, host, port, timeout, password);
    }
}
