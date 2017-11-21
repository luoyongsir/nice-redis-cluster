package com.nice.redis.template;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;

/**
 * fastJson 序列化缓存对象
 *
 * @author Luo Yong
 * @date 2017-03-12
 */
public class RedisTemplateJson extends AbstractTemplate {

	public RedisTemplateJson(JedisConnectionFactory jedisConnectionFactory) {
		super();
		template.setConnectionFactory(jedisConnectionFactory);
		// key只用String序列化
		template.setKeySerializer(template.getStringSerializer());
		// fastJson 序列化
		template.setDefaultSerializer(new GenericFastJsonRedisSerializer());
		template.afterPropertiesSet();
		serializer = (RedisSerializer<Object>) template.getDefaultSerializer();
	}

}
