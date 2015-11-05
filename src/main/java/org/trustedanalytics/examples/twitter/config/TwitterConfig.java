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

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class TwitterConfig {

    @Value("${twitter.consumer.key}")
    String consumerKey;

    @Value("${twitter.consumer.secret}")
    String consumerSecret;

    @Value("${twitter.token}")
    String token;

    @Value("${twitter.secret}")
    String secret;

    @Value("${twitter.terms}")
    String[] terms;


    @Bean
    BlockingQueue<String> blockingQueue() {
        return new LinkedBlockingQueue<String>(10000);
    }

    @Bean
    @Autowired
    Client twitterClient(@Value("#{blockingQueue}") BlockingQueue<String> blockingQueue) {
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        // add some track terms
        endpoint.trackTerms(Lists.newArrayList(terms));

        Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);

        Client client = new ClientBuilder().hosts(Constants.STREAM_HOST).endpoint(endpoint).authentication(auth)
                .processor(new StringDelimitedProcessor(blockingQueue)).build();

        return client;
    }

    @Override
    public String toString() {
        return "TwitterConfig{" +
                "consumerKey='" + consumerKey + '\'' +
                ", token='" + token + '\'' +
                ", terms=" + Arrays.toString(terms) +
                '}';
    }
}
