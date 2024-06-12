# A Billion Events Challenge

This is a version of the popular [One Billion Row Challenge](https://github.com/gunnarmorling/1brc).
The goal is to consume and process a billion events from a kafka cluster.

## helpfull commands
```
# docker container
docker pull apache/kafka:3.7.0

docker run -p 9092:9092 apache/kafka:3.7.0

# create topic
kafka-topics --create --topic <topic> --bootstrap-server localhost:9092


# manually produce events
kafka-console-producer --topic <topic> --bootstrap-server localhost:9092

#manually consume events
kafka-console-consumer --topic <topic> --bootstrap-server localhost:9092

```

## Tracking Time

Because this challenge works around a streaming application we have to declare a way to measure the time.
The time I will count for myself is the time between the first event published and the last element consumed. 
To restate the requirements:

1. Publish 1 billion events to kafka
2. Process the prior created events and calculate the average temperature

Other than this the challenge is to learn something :) 

## Starting point

Docker kafka setup
```
# out of the box container
docker run -p 9092:9092 apache/kafka:3.7.0

# create topic
kafka-topics --create --topic billion-events-topic-0 --bootstrap-server localhost:9092
```

Process: 
1. Creating the cluster container and topic
2. starting the consumer
3. starting the publisher

| All times <br/>in ms | First Event | Mil Event | time | includes<br/> publishing time |
|----------------------|-------------|-----------|------|-------------------------------|
| 1                    | 7158        | 10631     | 3473 | 2221                          |
| 2                    | 9081        | 12375     | 3294 | 1965                          |
| 3                    | 6234        | 9639      | 3405 | 1992                          |

Average time: 3390ms

## Changing partitions
