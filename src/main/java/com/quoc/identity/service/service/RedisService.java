package com.quoc.identity.service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisService {

    StringRedisTemplate redisTemplate;

    private static final String INVALIDATED_TOKEN_PREFIX = "invalidated-token:";

    public void invalidateToken(String jti, Duration ttl) {

        if (jti == null || jti.isBlank()) {
            return;
        }

        if (ttl.isNegative() || ttl.isZero()) {
            return;
        }

        String key = INVALIDATED_TOKEN_PREFIX + jti;

        redisTemplate.opsForValue().set(
                key,
                "true",
                ttl
        );
    }

    public boolean isTokenInvalidated(String jti) {

        if (jti == null || jti.isBlank()) {
            return false;
        }

        String key = INVALIDATED_TOKEN_PREFIX + jti;

        return Boolean.TRUE.equals(
                redisTemplate.hasKey(key)
        );
    }
}