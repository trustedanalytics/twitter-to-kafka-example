/*
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.trustedanalytics.examples.twitter.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
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
    @Value("${kafka.key.serializer.class:rg.apache.kafka.common.serialization.StringSerializer}")
    public String keySerializer;
    @Value("${kafka.value.serializer.class:org.apache.kafka.common.serialization.StringSerializer}")
    public String valueSerializer;

    @Autowired
    Environment env;

    @Bean
    String topic() {
        return env.getProperty("kafka.topic");
    }

    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        Properties producerConfig = new Properties();
        //http://kafka.apache.org/documentation.html#producerconfigs
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

        return new KafkaProducer<String, String>(producerConfig);
    }
}
