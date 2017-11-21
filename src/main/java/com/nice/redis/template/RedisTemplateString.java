package com.nice.redis.template;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * String序列化缓存对象
 *
 * @author Luo Yong
 * @date 2017-03-12
 */
public class RedisTemplateString extends AbstractTemplate {

	public RedisTemplateString(JedisConnectionFactory jedisConnectionFactory) {
		super();
		template.setConnectionFactory(jedisConnectionFactory);
		template.setDefaultSerializer(template.getStringSerializer());
		template.afterPropertiesSet();
		serializer = (RedisSerializer<Object>) template.getDefaultSerializer();
	}
}
