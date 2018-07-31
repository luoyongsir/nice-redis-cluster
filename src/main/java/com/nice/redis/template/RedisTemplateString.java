package com.nice.redis.template;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Collections;

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

    /**
     * 锁的默认过期时间，单位毫秒
     */
    private static final long DEFAULT_LOCK_TIME_OUT = 30 * 1000L;
    private static final long DEFAULT_TRY_LOCK_TIME_OUT = 1000L;

    /**
     * 拿锁的EVAL函数
     */
    private static final String LOCK_LUA_SCRIPT = "return redis.call('SET', KEYS[1], ARGV[1], 'NX', 'PX', ARGV[2]) ";
    private static RedisScript<String> lockScript = new DefaultRedisScript<>(LOCK_LUA_SCRIPT, String.class);

    /**
     * 释放锁的EVAL函数
     */
    private static final String UNLOCK_LUA_SCRIPT = "if (redis.call('GET', KEYS[1]) == ARGV[1]) then return redis.call('DEL',KEYS[1]) else return 0 end";
    private static RedisScript<String> unlockScript = new DefaultRedisScript<>(UNLOCK_LUA_SCRIPT, String.class);

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
        // 后面的三个参数分别对应了scriptLock字符串中的三个变量值，KEYS[1]，ARGV[1]，ARGV[2]，含义为锁的key，key对应的value，以及key的存在时间(单位毫秒)
        String result = template.execute(lockScript, Collections.singletonList(lockKey), lockValue, expireTimeMs.toString());
        // 返回“OK”代表拿到锁
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
     * @param lockValue 能区分请求的唯一标识
     * @return 是否释放成功
     */
    public boolean unLock(String lockKey, String lockValue) {
        String result = template.execute(unlockScript, Collections.singletonList(lockKey), lockValue);
        return OK.equals(result);
    }
}
