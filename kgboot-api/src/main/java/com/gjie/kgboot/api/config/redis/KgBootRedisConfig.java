package com.gjie.kgboot.api.config.redis;

import com.gjie.kgboot.api.client.redis.KgBootRedisClient;
import org.redisson.Redisson;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(KgBootRedisProperties.class)
@ConditionalOnProperty(name = "kgboot.enable.redis", havingValue = "true")
public class KgBootRedisConfig {
    @Autowired
    private KgBootRedisProperties kgBootRedisProperties;

    @Value(("${spring.redis.cluster.nodes}"))
    private String redisNodes;

    @Bean
    public KgBootRedisClient kgBootRedisClient() {
        KgBootRedisClient kgBootRedisClient = new KgBootRedisClient();
        kgBootRedisClient.setDelayDeleteTime(kgBootRedisProperties.getDelayTime() == null ?
                200l : kgBootRedisProperties.getDelayTime());
        return kgBootRedisClient;
    }

    @Bean
    @ConditionalOnProperty("spring.redis.cluster.nodes")
    public Redisson redisson() {
        //获取ip地址
        String[] nodes = redisNodes.split(",");
        //redisson版本是3.5，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
        List<String> clusterNodes = new ArrayList<>();
        for (int i = 0; i < nodes.length; i++) {
            clusterNodes.add("redis://" + nodes[i]);
        }
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers()
                .addNodeAddress(clusterNodes.toArray(new String[clusterNodes.size()]));
//        clusterServersConfig.setPassword(redisConfigProperties.getPassword());//设置密码，如果没有密码，则注释这一行，否则启动会报错
        return (Redisson) Redisson.create(config);
    }

}
