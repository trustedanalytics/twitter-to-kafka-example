package org.trustedanalytics.examples.twitter.config;

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Properties;

@Configuration
public class KafkaConfig {
    @Value("${kafka.brokers}")
    public String brokers;
    @Value("${kafka.serializer.class:kafka.serializer.StringEncoder}")
    public String serializer;

    @Autowired
    Environment env;

    @Bean
    String topic() {
        return env.getProperty("kafka.topic");
    }

    @Bean
    public Producer<String, String> getKafkaProducer() {
        System.out.println("getkafka producer " + brokers + " " + serializer);
        Properties properties = new Properties();

        properties.put("metadata.broker.list", brokers);
        properties.put("serializer.class", serializer);
        ProducerConfig producerConfig = new ProducerConfig(properties);
        Producer<String, String> producer = new Producer<String, String>(producerConfig);

        return producer;
    }
}
