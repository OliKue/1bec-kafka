package com.example;

import com.example.util.Cities;
import com.example.util.Event;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Random;

public class EventProducer {

    static final String[] cities = Cities.getCities();
    public static void main(String[] args) throws UnknownHostException {
        Properties config = new Properties();
        config.put("client.id", InetAddress.getLocalHost().getHostName());
        config.put("bootstrap.servers", "localhost:9092");
        config.put("acks", "all");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        final String topic = "billion-events-topic-0";


        try (final Producer<String, String> producer = new KafkaProducer<>(config)) {
            final long numMessages = 1000001L;
            long startTime = System.currentTimeMillis();
            System.out.printf("Starting to publish messages to %s. Sending %s messages%n",topic, numMessages);

            for (long i = 0L; i < numMessages; i++) {
                Event event = generateEvent();

                producer.send(
                        new ProducerRecord<>(topic, event.toString()),
                        (meta, ex) -> {
                            if (ex != null)
                                ex.printStackTrace();
                        });
            }

            long endTime = System.currentTimeMillis();
            System.out.printf("%s events were produced to topic %s in %s milliseconds%n", numMessages, topic, endTime-startTime);
        }
    }

    private static Event generateEvent(){
        String station = generateRandomCity();
        float weather = generateRandomWeather();
        return new Event(station, weather);
    }

    private static String generateRandomCity(){
        Random random = new Random();
        return cities[random.nextInt(cities.length)];
    }

    private static float generateRandomWeather(){
        Random random = new Random();
        if(random.nextFloat()>0.5)
            return random.nextFloat() * 10;
        else
            return 0 - random.nextFloat() * 10;
    }
}
