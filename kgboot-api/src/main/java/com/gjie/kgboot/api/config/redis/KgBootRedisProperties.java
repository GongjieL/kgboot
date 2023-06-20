package com.gjie.kgboot.api.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kgboot.redis")
public class KgBootRedisProperties {
    /**
     * 延迟双删时间
     */
    private Long delayTime;

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }
}
