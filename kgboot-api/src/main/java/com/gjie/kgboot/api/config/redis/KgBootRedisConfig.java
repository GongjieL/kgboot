package com.gjie.kgboot.api.config.redis;

import com.gjie.kgboot.api.client.redis.KgBootRedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KgBootRedisProperties.class)
@ConditionalOnProperty(name = "kgboot.enable.redis", havingValue = "true")
public class KgBootRedisConfig {
    @Autowired
    private KgBootRedisProperties kgBootRedisProperties;

    @Bean
    public KgBootRedisClient kgBootRedisClient() {
        KgBootRedisClient kgBootRedisClient = new KgBootRedisClient();
        kgBootRedisClient.setDelayDeleteTime(kgBootRedisProperties.getDelayTime() == null ?
                200l : kgBootRedisProperties.getDelayTime());
        return kgBootRedisClient;
    }
}
