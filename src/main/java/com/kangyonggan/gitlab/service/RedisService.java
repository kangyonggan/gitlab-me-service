package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.constants.RedisKeys;
import com.kangyonggan.gitlab.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author kyg
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * set
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @param value
     */
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * get
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * get
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Object get(String key, Object defaultValue) {
        Object val = redisTemplate.opsForValue().get(key);
        return val == null ? defaultValue : val;
    }

    /**
     * hashValues
     *
     * @param hash
     * @return
     */
    public List<Object> hashValues(String hash) {
        return redisTemplate.opsForHash().values(hash);
    }

    /**
     * hashGet
     *
     * @param hash
     * @param key
     * @return
     */
    public Object hashGet(String hash, Object key) {
        return redisTemplate.opsForHash().get(hash, key);
    }

    /**
     * get and update expire
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Object getAndUpdateExpire(String key, long timeout, TimeUnit unit) {
        Object object = redisTemplate.opsForValue().get(key);
        if (object != null) {
            redisTemplate.expire(key, timeout, unit);
        }
        return object;
    }

    /**
     * multiGet
     *
     * @param keys
     * @return
     */
    public List<Object> multiGet(Set<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * delete
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * delete all like pattern
     *
     * @param pattern
     * @return
     */
    public void deleteAll(String pattern) {
        redisTemplate.delete(redisTemplate.keys(pattern));
    }

    /**
     * increment
     *
     * @param key
     * @return
     */
    public long increment(String key) {
        return redisTemplate.opsForValue().increment(key, 1);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public long leftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * @param key
     * @param values
     * @return
     */
    public long leftPushAll(String key, Object... values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * @param key
     * @return
     */
    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * range
     *
     * @param key
     * @return
     */
    public List<Object> range(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * hashSetNx
     *
     * @param hash
     * @param key
     * @param value
     * @return
     */
    public boolean putIfAbsent(String hash, String key, String value) {
        return redisTemplate.opsForHash().putIfAbsent(hash, key, value);
    }

    /**
     * size
     *
     * @param hash
     * @return
     */
    public long size(String hash) {
        return redisTemplate.opsForHash().size(hash);
    }

    /**
     * hashExist
     *
     * @param hash
     * @param key
     * @return
     */
    public boolean hasKey(String hash, String key) {
        return redisTemplate.opsForHash().hasKey(hash, key);
    }

    /**
     * get keys of pattern
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 获取自增序列名
     *
     * @return
     */
    public String getIncrSerialNo() {
        return getIncrSerialNo(null);
    }

    /**
     * 获取自增序列名
     *
     * @param prefix
     * @return
     */
    public String getIncrSerialNo(String prefix) {
        if (StringUtils.isNotEmpty(prefix)) {
            prefix += "_";
        } else {
            prefix = "";
        }

        String nextVal = String.valueOf(increment(RedisKeys.INCREMENT_INDEX) % 100000000);
        String currentDate = DateUtil.format(new Date(), "yyyyMMdd");

        return prefix + currentDate + StringUtils.leftPad(nextVal, 8, "0");
    }

    /**
     * 获取随机序列名
     *
     * @return
     */
    public String getRandSerialNo() {
        String nextVal = String.valueOf(increment(RedisKeys.INCREMENT_INDEX) % 10000);
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16)
                + StringUtils.leftPad(nextVal, 4, "0");
    }

}
