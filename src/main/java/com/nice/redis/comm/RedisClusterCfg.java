package com.nice.redis.comm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Redis 配置文件
 * 
 * @author Luo Yong
 * @date 2017-03-12
 */
@Component
public class RedisClusterCfg {

	@Value("${redis.nodes}")
	private String nodes;
	
	@Value("${redis.max-redirects}")
	private Integer maxRedirects;
	
	@Value("${redis.minIdle}")
	private Integer minIdle;
	
	@Value("${redis.maxIdle}")
	private Integer maxIdle;
	
	@Value("${redis.maxTotal}")
	private Integer maxTotal;
	
	@Value("${redis.maxWaitMillis}")
	private Integer maxWaitMillis;
	
	@Value("${redis.testOnBorrow}")
	private String testOnBorrow;
	
	@Value("${redis.password}")
	private String password;

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public Integer getMaxRedirects() {
		return maxRedirects;
	}

	public void setMaxRedirects(Integer maxRedirects) {
		this.maxRedirects = maxRedirects;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public Integer getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	public Integer getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}

	public Integer getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(Integer maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public String getTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(String testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}