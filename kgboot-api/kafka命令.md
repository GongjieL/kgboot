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