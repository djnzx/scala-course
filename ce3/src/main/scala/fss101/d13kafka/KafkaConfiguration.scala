package fss101.d13kafka

import java.util.UUID

trait KafkaConfiguration {

  val kafkaIp = "localhost:9092"
  val schemaRegistryUrl = "http://localhost:8081"

  val topicPlain = "my-topic"
  val topicAvro = "my-topic-avro"

  val consumerGroupId = "group-".concat(UUID.randomUUID().toString)

}
