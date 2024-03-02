package com.example.techtaskagencyamazon.configurations;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuration class for setting up the cache manager with Caffeine.
 */
@Configuration
public class CacheConfig {
    /**
     * Cache lifetime
     */
    private static final int EXPIRATION_TIME_MINUTES = 5;
    /**
     * Maximum cache size
     */
    private static final int MAXIMUM_CACHE_SIZE = 100;

    /**
     * Bean for the cache manager.
     * It sets up a Caffeine cache manager with a specified expiration time and maximum size.
     *
     * @return a configured CacheManager instance
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(EXPIRATION_TIME_MINUTES, TimeUnit.MINUTES)
                .maximumSize(MAXIMUM_CACHE_SIZE));
        return cacheManager;
    }
}
