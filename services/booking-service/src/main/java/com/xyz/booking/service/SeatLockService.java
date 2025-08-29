package com.xyz.booking.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.UUID;

@Service
public class SeatLockService {
    private final StringRedisTemplate redis;
    private static final Duration TTL = Duration.ofMinutes(5);
    public SeatLockService(StringRedisTemplate redis){ this.redis = redis; }
    public String tryLock(String key){
        String token = UUID.randomUUID().toString();
        Boolean ok = redis.opsForValue().setIfAbsent(key, token, TTL);
        return Boolean.TRUE.equals(ok) ? token : null;
    }
    public boolean unlockIfOwner(String key, String token){
        String v = redis.opsForValue().get(key);
        if(v != null && v.equals(token)){ redis.delete(key); return true; }
        return false;
    }
}
