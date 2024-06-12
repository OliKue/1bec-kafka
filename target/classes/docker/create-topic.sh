#!/bin/bash

# Wait for Kafka to start
sleep 20

# Create the topic
/opt/kafka/bin/kafka-topics.sh --create --topic billion-events-topic-0 --bootstrap-server localhost:9092  --replication-factor 1 #--partitions 5

# Keep the container running
tail -f /dev/null
