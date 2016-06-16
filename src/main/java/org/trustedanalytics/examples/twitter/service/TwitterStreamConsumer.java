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

import com.google.gson.Gson;
import com.twitter.hbc.core.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.trustedanalytics.examples.twitter.model.TwitterMessage;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;

/**
 * The asynchronous Twitter stream consumer that sends data to the Kafka service.
 */
@Service
@EnableAsync
public class TwitterStreamConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterStreamConsumer.class);

    @Resource
    private BlockingQueue<String> blockingQueue;

    @Autowired
    private Client twitterClient;

    @Autowired
    private KafkaService kafkaService;

    /**
     * This method is the real consumer of the Twitter stream.
     * It connects to Twitter and waits for the incoming messages from Twitter.
     * Once it receives a Twitter message, it parses the message
     * and forwards the message text to the Kafka service.
     *
     * @throws InterruptedException if interrupted while waiting for the incoming Twitter message
     */
    @Async
    public void run() throws InterruptedException {
        LOG.info("Twitter data feed consumption started");

        twitterClient.connect();

        Gson gson = new Gson();

        while (!twitterClient.isDone()) {
            String jsonMessage = blockingQueue.take();
            LOG.debug("Received json msg: {}", jsonMessage);

            TwitterMessage twitterMessage = gson.fromJson(jsonMessage, TwitterMessage.class);
            LOG.debug("Parsed twitter msg: {}", twitterMessage);

            kafkaService.send(twitterMessage.getText());
        }
    }

    @PreDestroy
    protected void destroy() {
        twitterClient.stop();
    }

}
