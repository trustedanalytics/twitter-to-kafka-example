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

package org.trustedanalytics.examples.twitter.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class KafkaService {

    @Autowired
    KafkaProducer<String, String> kafkaProducer;

    @Autowired
    String topic;

    public void send(String payload) {
        kafkaProducer.send(new ProducerRecord<String, String>(topic, payload));
    }

    @PreDestroy
    protected void finalize() {
        kafkaProducer.close();
    }

    @Override
    public String toString() {
        return "KafkaService{" +
                "producer=" + kafkaProducer +
                ", topic='" + topic + '\'' +
                '}';
    }
}
