package com.nice.redis.template;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * jdk 自带序列化缓存对象
 *
 * @author Luo Yong
 * @date 2019-03-01
 */
public class RedisTemplateJdk extends AbstractTemplate {

    public RedisTemplateJdk(LettuceConnectionFactory lettuceConnectionFactory) {
        super();
        template.setConnectionFactory(lettuceConnectionFactory);
        template.setKeySerializer(template.getStringSerializer());
        template.afterPropertiesSet();
        serializer = (RedisSerializer<Object>) template.getDefaultSerializer();
    }
}
