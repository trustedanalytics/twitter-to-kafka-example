package org.trustedanalytics.examples.twitter.component;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class TwitterKafkaProducer {

    @Autowired
    Producer<String, String> producer;

    @Autowired
    String topic;

    @PostConstruct
    protected void initialize() {
        System.out.println("---------- twitter stream example");
        System.out.println(this.toString());
    }

    public void send(String payload) {
        KeyedMessage<String, String> message = new KeyedMessage<String, String>(topic, payload);
        producer.send(message);
    }

    @PreDestroy
    protected void finalize() {
        producer.close();
    }


    @Override
    public String toString() {
        return "TwitterKafkaProducer{" +
                "producer=" + producer +
                ", topic='" + topic + '\'' +
                '}';
    }
}
