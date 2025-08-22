package com.library.users.config;

import java.time.Duration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, Boolean> jwtBlacklist() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(20)) // token TTL + buffer
                .build();
    }
}
