# 说明
- 1、引用该starter的配置均为kgboot开头 
- 2、kgboot.enable.http(/kafka/rabbit/redis确认是否开启功能)
- 3、kgboot.api.trace-id-key 配置traceId的关键字，比如traceId，便于跟踪日志
- 4、kgboot.kafka.consumer.topics 配置多个topic监听，继承AbstractKafkaConsumerProcessor完成自己的消费



# kafka命令
## 机器创建集群
### 创建zk
<pre><code>docker run -d --name zookeeper -p 2181:2181 -t wurstmeister/zookeeper
</code></pre>
### 创建集群
<pre><code>docker run -d --name kafka0 -p 9092:9092 -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=(zk的ip):2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://公网ip:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -t wurstmeister/kafka
</code></pre>
<pre><code>docker run -d --name kafka1 -p 9093:9093 -e KAFKA_BROKER_ID=1 -e KAFKA_ZOOKEEPER_CONNECT=(zk的ip):2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://公网ip:9093 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9093 -t wurstmeister/kafka
</code></pre>
<pre><code>docker run -d --name kafka2 -p 9094:9094 -e KAFKA_BROKER_ID=2 -e KAFKA_ZOOKEEPER_CONNECT=(zk的ip):2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://公网ip:9094 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9094 -t wurstmeister/kafka
</code></pre>
## 生产者
### 创建主题
<pre><code>
./bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 2 --partitions 3 --topic hello-topic2
</code></pre>
### 生产消息

<pre><code>
/opt/kafka# bin/kafka-console-producer.sh --broker-list localhost:9092 --topic example-topic2</code></pre>
输入命令后，可以在控制台输入具体的消息内容

## 消费者
### 消费消息(查询主题中消息内容)
<pre><code>
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic hello5 --from-beginning
</code></pre>

# redis命令
## docker建立集群
### 1、创建redis集群机器
<pre><code>docker run -d --name redis-node-1 --net host --privileged=true -p 6381:6381 -v /docker/redis/share/redis-node-1:/data redis:6.2.2 --cluster-enabled yes --appendonly yes --port 6381

docker run -d --name redis-node-2 --net host --privileged=true -p 6382:6382 -v /docker/redis/share/redis-node-2:/data redis:6.2.2 --cluster-enabled yes --appendonly yes --port 6382

docker run -d --name redis-node-3 --net host --privileged=true -p 6383:6383 -v /docker/redis/share/redis-node-3:/data redis:6.2.2 --cluster-enabled yes --appendonly yes --port 6383

docker run -d --name redis-node-4 --net host --privileged=true -p 6384:6384 -v /docker/redis/share/redis-node-4:/data redis:6.2.2 --cluster-enabled yes --appendonly yes --port 6384

docker run -d --name redis-node-5 --net host --privileged=true -p 6385:6385 -v /docker/redis/share/redis-node-5:/data redis:6.2.2 --cluster-enabled yes --appendonly yes --port 6385

docker run -d --name redis-node-6 --net host --privileged=true -p 6386:6386 -v /docker/redis/share/redis-node-6:/data redis:6.2.2 --cluster-enabled yes --appendonly yes --port 6386
</code></pre>
### 2、打开redis端口和总线端口（redis端口+10000）
### 3、创建集群
<pre><code>redis-cli --cluster create 124.221.161.34:6381 124.221.161.34:6382 124.221.161.34:6383 124.221.161.34:6384 124.221.161.34:6385 124.221.161.34:6386 --cluster-replicas 1
</code></pre>
### 4、查看集群状态
- 1、redis客户端
<pre><code>redis-cli -p 6381 
</code></pre>
- 2、查看集群信息
<pre><code>cluster info
cluster nodes
</code></pre>
## 集群扩容
### 1、创建redis节点
<pre><code>docker run -d --name redis-node-7 --net host --privileged=true -p 6387:6387 -v /docker/redis/share/redis-node-7:/data redis:6.2.2 --cluster-enabled yes --appendonly yes --port 6387
</code></pre>
### 2、加入集群
- 加入master节点
<pre><code>redis-cli --cluster add-node 124.221.161.34:6387 124.221.161.34:6381
</code></pre>
- 加入slave节点
<pre><code>redis-cli --cluster add-node 124.221.161.34:6388 124.221.161.34:6381 --cluster-slave --cluster-master-id f8af5ce8a68ad237ef2afedd1b21178a47181800
</code></pre>
### 3、分配槽
<pre><code>redis-cli --cluster reshard 124.221.161.34:6383
</code></pre>
后续交互信息提示迁移地址，从哪些节点迁移
## 集群缩容
### 1、迁移分片
- 方案1：importing、migrating迁移（针对某一个槽） 
1、在迁移目标地址的机器上执行
<pre><code>cluster setslot 866 importing 462bd30e29650af584009d099c5e28a17da9a99c
</code></pre>
2、在迁移目标地址的机器上执行
<pre><code>cluster setslot 866 migrating 072222a8d4834f35e19ec95e901abc4b81bb5157
</code></pre>
3、获取槽上的key
<pre><code>cluster getkeysinslot 866 10
</code></pre>
4、迁移
<pre><code>migrate 124.221.161.34 6382 hello 0 1000
</code></pre>
- 方案2:reshard迁移
### 2、删除机器
<pre><code>redis-cli --cluster del-node 124.221.161.34:6383 462bd30e29650af584009d099c5e28a17da9a99c
</code></pre>










