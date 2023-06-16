package com.gjie.kgboot.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kgboot.api")
public class APICommonProperties {


    /**
     * 日志跟踪关键字
     */
    private String traceIdKey;


    public String getTraceIdKey() {
        return traceIdKey;
    }

    public void setTraceIdKey(String traceIdKey) {
        this.traceIdKey = traceIdKey;
    }
}
