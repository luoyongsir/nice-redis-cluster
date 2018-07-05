package com.nice.redis.template;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * jdk 自带序列化缓存对象
 *
 * @author Luo Yong
 * @date 2017-03-12
 */
public class RedisTemplateJdk extends AbstractTemplate {

    public RedisTemplateJdk(JedisConnectionFactory jedisConnectionFactory) {
        super();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(template.getStringSerializer());
        template.afterPropertiesSet();
        serializer = (RedisSerializer<Object>) template.getDefaultSerializer();
    }
}
