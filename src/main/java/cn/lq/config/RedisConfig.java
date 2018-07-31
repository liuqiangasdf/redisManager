package cn.lq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
	@Autowired
    private RedisClusterProperties redisClusterProperties;

	@Bean(name="jedisPoolConfig")
    public JedisPoolConfig initJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisClusterProperties.getPoolMaxIdle());
        jedisPoolConfig.setMaxWaitMillis(redisClusterProperties.getPoolMaxWait());
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(redisClusterProperties.getPoolTimeBetweenEvictionRunsMillis());
        jedisPoolConfig.setMinEvictableIdleTimeMillis(redisClusterProperties.getPoolMinEvictableIdleTimeMillis());
        jedisPoolConfig.setTestOnBorrow(redisClusterProperties.isPoolTestOnBorrow());
        jedisPoolConfig.setMaxIdle(redisClusterProperties.getPoolMaxIdle());
        return jedisPoolConfig;
    }
}
