package com.nice.redis.template;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * jackson 序列化缓存对象
 *
 * @author Luo Yong
 * @date 2019-03-01
 */
public class RedisTemplateJackson extends AbstractTemplate {

	public RedisTemplateJackson(LettuceConnectionFactory lettuceConnectionFactory) {
		super();
		template.setConnectionFactory(lettuceConnectionFactory);
		// key只用String序列化
		template.setKeySerializer(template.getStringSerializer());
		// jackson 序列化
		template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		template.afterPropertiesSet();
		serializer = (RedisSerializer<Object>) template.getDefaultSerializer();
	}

}
