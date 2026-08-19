package com.quoc.identity.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "blacklist:";

    public void invalidateToken(
            String jti,
            Duration duration
    ) {

        String key = PREFIX + jti;

        redisTemplate.opsForValue()
                .set(key, "invalid", duration);
    }

    public boolean isTokenInvalidated(String jti) {

        String key = PREFIX + jti;

        return Boolean.TRUE.equals(
                redisTemplate.hasKey(key)
        );
    }
}