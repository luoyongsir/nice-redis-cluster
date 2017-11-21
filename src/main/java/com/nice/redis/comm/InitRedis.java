package com.nice.redis.comm;

import com.nice.redis.template.RedisTemplateJackson;
import com.nice.redis.template.RedisTemplateJdk;
import com.nice.redis.template.RedisTemplateJson;
import com.nice.redis.template.RedisTemplateString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Set;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.StringUtils.commaDelimitedListToSet;
import static org.springframework.util.StringUtils.split;

/**
 * Redis初始化
 *
 * @author Luo Yong
 * @date 2017-03-12
 */
@Component
public class InitRedis {

	@Autowired
	private ResourcePropertySource resourcePropertySource;

	@Autowired
	private RedisClusterConfiguration redisClusterConfiguration;

	@Autowired
	private JedisPoolConfig jedisPoolConfig;

	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;

	/**
	 * 配置文件加载
	 *
	 * @return
	 * @throws IOException
	 */
	@Bean
	public ResourcePropertySource resourcePropertySource() throws IOException {
		return new ResourcePropertySource("redis.properties", "classpath:env/redis.properties");
	}

	/**
	 * 初始化 redis cluster 配置，注入bean到Spring
	 *
	 * @return
	 */
	@Bean
	public RedisClusterConfiguration redisClusterConfiguration() {
		RedisClusterConfiguration configuration = new RedisClusterConfiguration(resourcePropertySource);

		Object nodes = resourcePropertySource.getProperty("redis.nodes");
		if (nodes != null) {
			Set<String> hostAndPorts = commaDelimitedListToSet(nodes.toString());
			for (String hostAndPort : hostAndPorts) {
				String[] args = split(hostAndPort, ":");
				notNull(args, "HostAndPort need to be seperated by  ':'.");
				isTrue(args.length == 2, "Host and Port String needs to specified as host:port");
				RedisNode node = new RedisNode(args[0], Integer.parseInt(args[1]));
				configuration.addClusterNode(node);
			}
		}

		Object maxRedirects = resourcePropertySource.getProperty("redis.max-redirects");
		if (maxRedirects != null) {
			configuration.setMaxRedirects(NumberUtils.parseNumber(maxRedirects.toString(), Integer.class));
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
		Object minIdle = resourcePropertySource.getProperty("redis.minIdle");
		if (minIdle != null) {
			config.setMinIdle(Integer.parseInt(minIdle.toString()));
		}
		Object maxIdle = resourcePropertySource.getProperty("redis.maxIdle");
		if (maxIdle != null) {
			config.setMaxIdle(Integer.parseInt(maxIdle.toString()));
		}
		Object maxTotal = resourcePropertySource.getProperty("redis.maxTotal");
		if (maxTotal != null) {
			config.setMaxTotal(Integer.parseInt(maxTotal.toString()));
		}
		Object maxWaitMillis = resourcePropertySource.getProperty("redis.maxWaitMillis");
		if (maxWaitMillis != null) {
			config.setMaxWaitMillis(Integer.parseInt(maxWaitMillis.toString()));
		}
		Object testOnBorrow = resourcePropertySource.getProperty("redis.testOnBorrow");
		if (testOnBorrow != null) {
			config.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow.toString()));
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
		Object password = resourcePropertySource.getProperty("redis.password");
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
