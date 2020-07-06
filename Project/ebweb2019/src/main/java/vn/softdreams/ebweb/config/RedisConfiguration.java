package vn.softdreams.ebweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author hieug
 */
@Configuration
public class RedisConfiguration {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMinIdle(2);
        poolConfig.setMaxIdle(5);
//        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jedisPoolingClientConfigurationBuilder = JedisClientConfiguration.builder().usePooling();
//        jedisPoolingClientConfigurationBuilder.

//        redis.setHostName("172.16.10.69");
//        redis.setPassword("redisCiCb2018");

        JedisConnectionFactory redis =  new JedisConnectionFactory();
        redis.setHostName("172.16.10.72");
        redis.setPassword("Kt!@#202o@");
        redis.setPort(6379);
        redis.setDatabase(3);
        redis.setUsePool(true);
        redis.setPoolConfig(poolConfig);

        return redis;
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer ());
        template.setValueSerializer(new StringRedisSerializer ());
        template.setHashKeySerializer(new StringRedisSerializer ());
        template.setHashValueSerializer(new StringRedisSerializer ());
        template.setEnableTransactionSupport(true);
//        template.setEnableDefaultSerializer(false);
        return template;
    }
}
