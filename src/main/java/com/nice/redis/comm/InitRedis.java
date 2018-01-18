package com.nice.redis.comm;

import com.nice.redis.template.RedisTemplateJackson;
import com.nice.redis.template.RedisTemplateJdk;
import com.nice.redis.template.RedisTemplateJson;
import com.nice.redis.template.RedisTemplateString;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.StringUtils.commaDelimitedListToSet;

/**
 * Redis初始化
 *
 * @author Luo Yong
 * @date 2017-03-12
 */
@Configuration
public class InitRedis {

	/**
	 * 必填配置
	 */
	@Value("${redis.nodes}")
	private String nodes;

	@Value("${redis.maxRedirects:}")
	private Integer maxRedirects;

	@Value("${redis.minIdle:}")
	private Integer minIdle;

	@Value("${redis.maxIdle:}")
	private Integer maxIdle;

	@Value("${redis.maxTotal:}")
	private Integer maxTotal;

	@Value("${redis.maxWaitMillis:}")
	private Integer maxWaitMillis;

	@Value("${redis.testOnBorrow:}")
	private Boolean testOnBorrow;

	@Value("${redis.password:}")
	private String password;

	/**
	 * 初始化 redis cluster 配置，注入bean到Spring
	 *
	 * @return
	 */
	@Bean
	public RedisClusterConfiguration redisClusterConfiguration() {
		notNull(getNodes(), " redis.nodes must not null. ");
		Set<String> hostAndPorts = commaDelimitedListToSet(getNodes());
		RedisClusterConfiguration configuration = new RedisClusterConfiguration(hostAndPorts);
		Integer maxRedirects = getMaxRedirects();
		if (maxRedirects != null) {
			configuration.setMaxRedirects(maxRedirects);
		}
		return configuration;
	}

	/**
	 * 连接池配置
	 *
	 * @return
	 */
	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		Integer minIdle = getMinIdle();
		if (minIdle != null) {
			config.setMinIdle(minIdle);
		}
		Integer maxIdle = getMaxIdle();
		if (maxIdle != null) {
			config.setMaxIdle(maxIdle);
		}
		Integer maxTotal = getMaxTotal();
		if (maxTotal != null) {
			config.setMaxTotal(maxTotal);
		}
		Integer maxWaitMillis = getMaxWaitMillis();
		if (maxWaitMillis != null) {
			config.setMaxWaitMillis(maxWaitMillis);
		}
		Boolean testOnBorrow = getTestOnBorrow();
		if (testOnBorrow != null) {
			config.setTestOnBorrow(testOnBorrow);
		}
		return config;
	}

	/**
	 * 连接工厂
	 *
	 * @return
	 */
	@Bean
	public JedisConnectionFactory jedisConnectionFactory(
			@Qualifier("redisClusterConfiguration") RedisClusterConfiguration redisClusterConfiguration,
			@Qualifier("jedisPoolConfig") JedisPoolConfig jedisPoolConfig) {
		JedisConnectionFactory factory = new JedisConnectionFactory(redisClusterConfiguration, jedisPoolConfig);
		String password = getPassword();
		if (password != null && !StringUtils.isEmpty(password.trim())) {
			factory.setPassword(password.trim());
		}
		return factory;
	}

	@Bean
	public RedisTemplateJackson redisTemplateJackson(
			@Qualifier("jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory) {
		return new RedisTemplateJackson(jedisConnectionFactory);
	}

	@Bean
	public RedisTemplateJdk redisTemplateJdk(
			@Qualifier("jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory) {
		return new RedisTemplateJdk(jedisConnectionFactory);
	}

	@Bean
	public RedisTemplateJson redisTemplateJson(
			@Qualifier("jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory) {
		return new RedisTemplateJson(jedisConnectionFactory);
	}

	@Bean
	public RedisTemplateString redisTemplateString(
			@Qualifier("jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory) {
		return new RedisTemplateString(jedisConnectionFactory);
	}

	public String getNodes() {
		return nodes;
	}

	public Integer getMaxRedirects() {
		return maxRedirects;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public Integer getMaxIdle() {
		return maxIdle;
	}

	public Integer getMaxTotal() {
		return maxTotal;
	}

	public Integer getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}

	public String getPassword() {
		return password;
	}
}
