package com.nice.redis.template;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * jackson 序列化缓存对象
 *
 * @author Luo Yong
 * @date 2017-03-12
 */
public class RedisTemplateJackson extends AbstractTemplate {

	public RedisTemplateJackson(JedisConnectionFactory jedisConnectionFactory) {
		super();
		template.setConnectionFactory(jedisConnectionFactory);
		// key只用String序列化
		template.setKeySerializer(template.getStringSerializer());
		// jackson 序列化
		template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		template.afterPropertiesSet();
		serializer = (RedisSerializer<Object>) template.getDefaultSerializer();
	}

}
