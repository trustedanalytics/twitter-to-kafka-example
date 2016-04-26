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
import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    @Value("${twitter.terms:}")
    String[] terms;

    @Value("${twitter.followings:}")
    String[] followings;

    @Value("${twitter.locations:}")
    String[] coordinates;

    @Bean
    BlockingQueue<String> blockingQueue() {
        return new LinkedBlockingQueue<>(10000);
    }

    @Bean
    @Autowired
    Client twitterClient(@Value("#{blockingQueue}") BlockingQueue<String> blockingQueue) throws Exception {
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();

        // add some filter conditions
        if (terms.length > 0) {
            endpoint.trackTerms(Lists.newArrayList(terms));
        }

        List<Long> userIds = parseFollowings(followings);
        if (userIds.size() > 0) {
            System.out.println("Followings: " + userIds);
            endpoint.followings(userIds);
        }

        List<Location> locations = parseLocations(coordinates);
        if (locations.size() > 0) {
            System.out.println("Locations: " + Lists.newArrayList(coordinates));
            System.out.println("Locations: " + locations);
            endpoint.locations(locations);
        }

        Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);

        return new ClientBuilder().hosts(Constants.STREAM_HOST).endpoint(endpoint).authentication(auth)
                .processor(new StringDelimitedProcessor(blockingQueue)).build();
    }

    private List<Long> parseFollowings(String[] followings) {
        List<Long> result = new ArrayList<>();

        if (followings == null) {
            System.out.println("null followings");
            return result;
        }

        Arrays.stream(followings).mapToLong(Long::parseLong).forEach(result::add);

        return result;
    }

    private List<Location> parseLocations(String[] locations) throws Exception {
        List<Location> result = new ArrayList<>();

        if (locations == null) {
            System.out.println("null locations");
            return result;
        }

        if (locations.length % 4 != 0) {
            throw new Exception("locations should be a list of pairs of longitude,latitude.");
        }

        for (int i = 0; i < locations.length / 4; i++) {
            result.add(new Location(
                            new Location.Coordinate(Double.parseDouble(locations[i * 2 + 0]), Double.parseDouble(locations[i * 2 + 1])),
                            new Location.Coordinate(Double.parseDouble(locations[i * 2 + 2]), Double.parseDouble(locations[i * 2 + 3]))
                    )
            );
        }
        return result;
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
