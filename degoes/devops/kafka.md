- [dockerHub link](https://hub.docker.com/r/confluent/kafka/)
- [gitHub link](https://github.com/confluentinc/docker-images)
- [full `docker-compose.yml` config](https://github.com/confluentinc/docker-images/blob/master/examples/fullstack/docker-compose.yml)
- [medium](https://medium.com/@oleksandra_a/apache-kafka-and-zio-af418b4c54f0)
- [kafka persistent](https://www.confluent.io/blog/okay-store-data-apache-kafka/)

download: `docker pull confluent/kafka`

##### Start Zookeeper and expose port 2181 for use by the host machine
`docker run -d --name zookeeper -p 2181:2181 confluent/zookeeper`

##### Start Kafka and expose port 9092 for use by the host machine
`docker run -d --name kafka -p 9092:9092 --link zookeeper:zookeeper confluent/kafka`

##### Start Schema Registry and expose port 8081 for use by the host machine
`docker run -d --name schema-registry -p 8081:8081 --link zookeeper:zookeeper \
   --link kafka:kafka confluent/schema-registry`

##### Start REST Proxy and expose port 8082 for use by the host machine
`docker run -d --name rest-proxy -p 8082:8082 --link zookeeper:zookeeper \
   --link kafka:kafka --link schema-registry:schema-registry confluent/rest-proxy`
