package com.example.demo.jdbc.cache;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import org.apache.ibatis.cache.Cache;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * 实现了Cache接口的类都可以作为mybatis二级缓存,该接口配合mybatis配置文件使用 基于 Redis 的缓存
 *
 * @since 1.0.0
 */
@Component("redisCache")
public class RedisCache implements Cache {


    private String id;
    
    /**以下三种redis使用 可以任选一种*/
    @Resource
    private JedisPool pool;
    
    @Resource
    private RedisTemplate redisTemplate;
    
    @Resource
    private JedisCluster jedisCluster;


    @Override
    public String getId() {
        return  "testCache";
    }

    @Override
    public void putObject(Object key, Object value) {
        try (Jedis jedis = pool.getResource()) {
            jedis.hset(id.getBytes(), key.toString().getBytes(), SerializationUtils.serialize(value) );
        }
    }

    @Override
    public Object getObject(Object key) {
        try (Jedis jedis = pool.getResource()) {
            return SerializationUtils.deserialize(jedis.hget(id.getBytes(), key.toString().getBytes()));
        }
    }

    @Override
    public Object removeObject(Object key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hdel(key.toString().getBytes());
        }
    }

    @Override
    public void clear() {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(id);
        }
    }

    @Override
    public int getSize() {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hgetAll(id).size();
        }
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return new ReentrantReadWriteLock();
    }
}
