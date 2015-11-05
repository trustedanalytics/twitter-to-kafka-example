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

import com.twitter.hbc.core.Client;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;


@Service
@EnableAsync
public class TwitterStreamConsumer {

    @Resource
    BlockingQueue<String> blockingQueue;

    @Autowired
    Client twitterClient;

    @Autowired
    KafkaService kafkaService;

    @Async
    public void run() throws InterruptedException, ParseException {
        twitterClient.connect();

        //XXX for debug purposes; remove
        int i = 0;

        JSONParser parser = new JSONParser();
        // Do whatever needs to be done with messages
//        for (int msgRead = 0; msgRead < 10000; msgRead++) {
        while (!twitterClient.isDone()) {
            String msg = blockingQueue.take();

            //XXX for debug purposes; remove
            System.out.println(++i + ". " +msg);

            JSONObject jsonObject = (JSONObject) parser.parse(msg);
            Object geo = jsonObject.get("geo");
            Object place = jsonObject.get("place");

            //XXX for debug purposes; remove
            if (geo != null || place != null) {
                System.out.println("-------------!!!!");
                //System.out.println(msg);
                System.out.println(geo);
                System.out.println(place);
            }

            kafkaService.send(msg);
        }
    }


    @PreDestroy
    protected void finalize() {
        twitterClient.stop();
    }

}
