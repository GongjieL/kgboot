package com.gjie.kgboot.api.config.http;

import com.gjie.kgboot.api.client.http.HttpApiClient;
import com.gjie.kgboot.api.strategy.http.CommonRespProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties({HttpClientProperties.class})
@ConditionalOnProperty(name = "kgboot.enable.http",havingValue = "true")
public class HttpApiConfig {
    @Autowired
    private HttpClientProperties httpClientProperties;
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        Integer timeout = httpClientProperties.getTimeout() == null ?
                6000 : httpClientProperties.getTimeout();
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(timeout);
        httpRequestFactory.setReadTimeout(timeout);
        return new RestTemplate();
    }

    /**
     * 执行http请求
     * @return
     */
    @Bean
    HttpApiClient httpApiClient() {
        return new HttpApiClient(restTemplate());
    }


    /**
     * 标准解析结果
     * @return
     */
    @Bean
    CommonRespProcessor commonRespProcessor() {
        return new CommonRespProcessor();
    }

}
