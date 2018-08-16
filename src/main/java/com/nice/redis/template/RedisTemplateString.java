package com.nice.redis.template;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.JedisCommands;

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

    private static final String OK = "OK";
    private static final String ONE = "1";

    /**
     * 锁的默认过期时间，单位毫秒
     */
    private static final long DEFAULT_LOCK_TIME_OUT = 30 * 1000L;
    private static final long DEFAULT_TRY_LOCK_TIME_OUT = 1000L;

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey      锁
     * @param lockValue    能区分请求的唯一标识
     * @param expireTimeMs 过期时间
     * @return 是否获取成功
     */
    public boolean lock(String lockKey, String lockValue, Long expireTimeMs) {
        if (expireTimeMs == null || expireTimeMs <= 0L) {
            expireTimeMs = DEFAULT_LOCK_TIME_OUT;
        }
        final long expire = expireTimeMs;
        String result = template.execute((RedisCallback<String>) con -> {
            JedisCommands commands = (JedisCommands) con.getNativeConnection();
            return commands.set(lockKey, lockValue, "NX", "PX", expire);
        });
        return OK.equals(result);
    }

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey          锁
     * @param lockValue        能区分请求的唯一标识
     * @param expireTimeMs     过期时间
     * @param tryLockTimeoutMs 尝试获取锁的时间，如果该时间段内未获取到锁，则返回false
     * @return 是否获取成功
     */
    public boolean lock(String lockKey, String lockValue, Long expireTimeMs, long tryLockTimeoutMs) {
        if (tryLockTimeoutMs <= 0L) {
            tryLockTimeoutMs = DEFAULT_TRY_LOCK_TIME_OUT;
        }
        long timeOut = System.currentTimeMillis() + tryLockTimeoutMs;
        // 在超时之前，循环尝试拿锁
        while (System.currentTimeMillis() < timeOut) {
            if (lock(lockKey, lockValue, expireTimeMs)) {
                return true;
            } else {
                // 等待50ms后继续尝试获取锁
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁
     * @return 是否释放成功
     */
    public boolean unLock(String lockKey) {
        Long res = del(lockKey);
        return ONE.equals(res);
    }
}
