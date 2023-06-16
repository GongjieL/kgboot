package com.gjie.kgboot.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "kgboot.kafka")
public class KafkaClientProperties {
    //-----生产者----
    /**
     * kafka集群地址
     */
    private String bootstrapServers;

    /**
     * 重试次数
     */
    private Integer retires;

    private String acks;
    /**
     * 最长阻塞时间
     */
    private Integer maxBlockMs;

    /**
     * 批量处理的最大大小
     */
    private Integer batchSize;
    /**
     * 发送延时,当生产端积累的消息达到batch-size或
     * 接收到消息linger.ms后,生产者就会将消息提交给kafka
     */
    private Integer lingerMs;

    /**
     * 生产者可用缓冲区的最大值 单位 byte
     */
    private Integer bufferMemory;

    /**
     * 每条消息最大的大小
     */
    private Integer maxRequestSize;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * key序列化方式
     */
    private String keySerializerClass;

    /**
     * value序列化方式
     */
    private String valueSerializerClass;

    /**
     * 消息压缩：none、lz4、gzip、snappy，默认为 none
     */
    private String compressionType;

    /**
     * 自定义分区器
     */
    private String partitionerClass;


    //----消费者---

    /**
     * 是否自动提交
     */
    private Boolean enableAutoCommit;
    /**
     * 自动提交间隔
     */
    private String autoCommitIntervalMs;
    /**
     * 批量消费最大数量
     */
    private String maxPollRecords;
    /**
     * 消费者组
     */
    private String groupId;
    /**
     * session超时，超过这个时间consumer没有发送心跳,就会触发rebalance操作
     */
    private String sessionTimeoutMs;
    /**
     * 请求超时
     */
    private String requestTimeoutMs;
    /**
     * key反序列化类
     */
    private String keyDeserializerClass;
    /**
     * value反序列化类
     */
    private String valueDeserializerClass;
    /**
     * //当kafka中没有初始offset或offset超出范围时将自动重置offset
     *     //earliest:重置为分区中最小的offset
     *     //latest:重置为分区中最新的offset(消费分区中新产生的数据)
     *     //none:只要有一个分区不存在已提交的offset,就抛出异常
     */
    private String autoOffsetReset;

    /**
     * Consumer拦截器
     */
    private String interceptorClasses;

    /**
     * 监听的kafka topic
     * @return
     */
    private List<String> consumerTopics;

    /**
     * 消费者线程
     * @return
     */
    private Integer consumerThreadNum;

    public Integer getConsumerThreadNum() {
        return consumerThreadNum;
    }

    public void setConsumerThreadNum(Integer consumerThreadNum) {
        this.consumerThreadNum = consumerThreadNum;
    }

    public List<String> getConsumerTopics() {
        return consumerTopics;
    }

    public void setConsumerTopics(List<String> consumerTopics) {
        this.consumerTopics = consumerTopics;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public Integer getRetires() {
        return retires;
    }

    public void setRetires(Integer retires) {
        this.retires = retires;
    }

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public Integer getMaxBlockMs() {
        return maxBlockMs;
    }

    public void setMaxBlockMs(Integer maxBlockMs) {
        this.maxBlockMs = maxBlockMs;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getLingerMs() {
        return lingerMs;
    }

    public void setLingerMs(Integer lingerMs) {
        this.lingerMs = lingerMs;
    }

    public Integer getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(Integer bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public Integer getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(Integer maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getKeySerializerClass() {
        return keySerializerClass;
    }

    public void setKeySerializerClass(String keySerializerClass) {
        this.keySerializerClass = keySerializerClass;
    }

    public String getValueSerializerClass() {
        return valueSerializerClass;
    }

    public void setValueSerializerClass(String valueSerializerClass) {
        this.valueSerializerClass = valueSerializerClass;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public String getPartitionerClass() {
        return partitionerClass;
    }

    public void setPartitionerClass(String partitionerClass) {
        this.partitionerClass = partitionerClass;
    }

    public Boolean getEnableAutoCommit() {
        return enableAutoCommit;
    }

    public void setEnableAutoCommit(Boolean enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }

    public String getAutoCommitIntervalMs() {
        return autoCommitIntervalMs;
    }

    public void setAutoCommitIntervalMs(String autoCommitIntervalMs) {
        this.autoCommitIntervalMs = autoCommitIntervalMs;
    }

    public String getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(String maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(String sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public String getRequestTimeoutMs() {
        return requestTimeoutMs;
    }

    public void setRequestTimeoutMs(String requestTimeoutMs) {
        this.requestTimeoutMs = requestTimeoutMs;
    }

    public String getKeyDeserializerClass() {
        return keyDeserializerClass;
    }

    public void setKeyDeserializerClass(String keyDeserializerClass) {
        this.keyDeserializerClass = keyDeserializerClass;
    }

    public String getValueDeserializerClass() {
        return valueDeserializerClass;
    }

    public void setValueDeserializerClass(String valueDeserializerClass) {
        this.valueDeserializerClass = valueDeserializerClass;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public String getInterceptorClasses() {
        return interceptorClasses;
    }

    public void setInterceptorClasses(String interceptorClasses) {
        this.interceptorClasses = interceptorClasses;
    }
}
