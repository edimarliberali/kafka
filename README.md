# Start Kafka Server and Zookeeper Server

## Kafka directory
```
/home/mtuser/Ferramentas/kafka/kafka_2.13-3.3.1
```

## Start Zookeeper
```
bin/zookeeper-server-start.sh config/zookeeper.properties
```

## Start Kafka
```
bin/kafka-server-start.sh config/server.properties
``` 
### Configurations in server.properties

The line below was uncommented in server.properties to resolve the 'Name or service not known' issue for start kafka
```
listeners=PLAINTEXT://127.0.0.1:9092
```

A comma separated list of directories under which to store log files
```
log.dirs=/home/mtuser/logs/kafka
```

The default number of log partitions per topic. More partitions allow greater
parallelism for consumption, but this will also result in more files across
the brokers.
```
num.partitions=3
```

Increase the number of replication in cluster
```
default.replication.factor=3
```

The replication factor for the group metadata internal topics "__consumer_offsets" and "__transaction_state"
For anything other than development testing, a value greater than 1 is recommended to ensure availability such as 3.
```
offsets.topic.replication.factor=3
transaction.state.log.replication.factor=3
```

## Commands

### Listar informações dos grupos de consumo - Lista as mensagens que foram recebidas, consumidas...
```
bin/kafka-consumer-groups.sh --all-groups --bootstrap-server localhost:9092 --describe
```

### Criar tópico
```
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic LOJA_NOVO_PEDIDO
```

### Listar tópicos
```
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

### Apresentar informações dos tópicos
```
bin/kafka-topics.sh --describe --bootstrap-server localhost:9092
```

### Enviar mensagens - producer
```
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic LOJA_NOVO_PEDIDO
vai ficar esperando o content da mensagem ser informado no console...
```

### Receber as mensagens - consumer
```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic LOJA_NOVO_PEDIDO [Recebe as mensagens a partir da execução do comando]
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic LOJA_NOVO_PEDIDO --from-beginning [Recebe as mensagens desde o inicio]
```