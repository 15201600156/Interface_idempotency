package com.study.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    StringRedisTemplate redisTemplate;
    public  boolean setNx(String key,String value)
    {
       return redisTemplate.opsForValue().setIfAbsent(key, value, 10, TimeUnit.SECONDS);
    }

    public void removeKey(String token) {
        redisTemplate.delete(token);
    }
}
