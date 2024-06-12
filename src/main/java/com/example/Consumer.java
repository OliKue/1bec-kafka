package com.example;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.util.Event;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

/**
 * Hello world!
 */
public class Consumer {
    public static void main(String[] args) {
        {
            Random random = new Random();

            Properties props = new Properties();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
            props.put(StreamsConfig.CLIENT_ID_CONFIG, "client-" + random.nextInt());
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            final String topic = "billion-events-topic-0";

            final StreamsBuilder builder = new StreamsBuilder();

            long startTime = System.currentTimeMillis();

            HashMap<String, List<Float>> weather = new HashMap<>();
            AtomicInteger counter = new AtomicInteger();

            builder.stream(topic)
                    //.peek((key, value) -> printTheEvent(key, (new Event(value.toString())).toString()))
                    .peek((key, value) -> counter.getAndIncrement())
                    .peek((key, value) -> {
                        if (counter.get() % 100000 == 1) {
                            System.out.printf("%s events were consumed from topic %s in %s milliseconds%n", counter, topic, System.currentTimeMillis() - startTime);
                        }
                    })
                    .foreach((key, value) -> {
                        Event event = new Event(value.toString());
                        if (weather.containsKey(event.getStation())) {
                            weather.get(event.getStation()).add(event.getWeather());
                        } else {
                            ArrayList<Float> list = new ArrayList<>();
                            list.add(event.getWeather());
                            weather.put(event.getStation(), list);
                        }
                    });

            final Topology topology = builder.build();
            final KafkaStreams streams = new KafkaStreams(topology, props);
            final CountDownLatch latch = new CountDownLatch(1);

            // attach shutdown handler to catch control-c
            Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
                @Override
                public void run() {
                    streams.close();
                    latch.countDown();
                }
            });

            try {
                streams.start();
                latch.await();
            } catch (Throwable e) {
                System.exit(1);
            }
            System.exit(0);
        }

    }
}
