package com.gjie.kgboot.api.config.redis;

import com.gjie.kgboot.api.client.redis.KgBootRedisClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "kgboot.enable.redis", havingValue = "true")
public class KgBootRedisConfig {
    @Bean
    public KgBootRedisClient kgBootRedisClient() {
        return new KgBootRedisClient();
    }
}
