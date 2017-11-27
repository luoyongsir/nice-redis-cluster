package com.nice.redis.comm;

import com.nice.redis.template.RedisTemplateJackson;
import com.nice.redis.template.RedisTemplateJdk;
import com.nice.redis.template.RedisTemplateJson;
import com.nice.redis.template.RedisTemplateString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.util.StringUtils.commaDelimitedListToSet;

/**
 * Redis初始化
 *
 * @author Luo Yong
 * @date 2017-03-12
 */
@Component
public class InitRedis {

	@Autowired
	private RedisClusterCfg redisConfig;

	@Autowired
	private RedisClusterConfiguration redisClusterConfiguration;

	@Autowired
	private JedisPoolConfig jedisPoolConfig;

	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;

	/**
	 * 初始化 redis cluster 配置，注入bean到Spring
	 *
	 * @return
	 */
	@Bean
	public RedisClusterConfiguration redisClusterConfiguration() {
		List<String> clusterNodes = new ArrayList<>();
		if (redisConfig.getNodes() != null) {
			Set<String> hostAndPorts = commaDelimitedListToSet(redisConfig.getNodes());
			for (String hostAndPort : hostAndPorts) {
				clusterNodes.add(hostAndPort);
			}
			RedisClusterConfiguration configuration = new RedisClusterConfiguration(hostAndPorts);
			Integer maxRedirects = redisConfig.getMaxRedirects();
			if (maxRedirects != null) {
				configuration.setMaxRedirects(maxRedirects);
			}
			return configuration;
		}
		return null;
	}

	/**
	 * 连接池配置
	 *
	 * @return
	 */
	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		Integer minIdle = redisConfig.getMinIdle();
		if (minIdle != null) {
			config.setMinIdle(minIdle);
		}
		Integer maxIdle = redisConfig.getMaxIdle();
		if (maxIdle != null) {
			config.setMaxIdle(maxIdle);
		}
		Integer maxTotal = redisConfig.getMaxTotal();
		if (maxTotal != null) {
			config.setMaxTotal(maxTotal);
		}
		Integer maxWaitMillis = redisConfig.getMaxWaitMillis();
		if (maxWaitMillis != null) {
			config.setMaxWaitMillis(maxWaitMillis);
		}
		String testOnBorrow = redisConfig.getTestOnBorrow();
		if (testOnBorrow != null) {
			config.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
		}
		return config;
	}

	/**
	 * 连接工厂
	 *
	 * @return
	 */
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory(redisClusterConfiguration, jedisPoolConfig);
		String password = redisConfig.getPassword();
		if (password != null) {
			factory.setPassword(password.toString());
		}
		return factory;
	}

	@Bean
	public RedisTemplateJackson redisTemplateJackson() {
		return new RedisTemplateJackson(jedisConnectionFactory);
	}

	@Bean
	public RedisTemplateJdk redisTemplateJdk() {
		return new RedisTemplateJdk(jedisConnectionFactory);
	}

	@Bean
	public RedisTemplateJson redisTemplateJson() {
		return new RedisTemplateJson(jedisConnectionFactory);
	}

	@Bean
	public RedisTemplateString redisTemplateString() {
		return new RedisTemplateString(jedisConnectionFactory);
	}

	private InitRedis() {
	}
}
