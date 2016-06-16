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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * This component is a starting point of Twitter data feed consumption.
 */
@Component
public class Initializer {

    private static final Logger LOG = LoggerFactory.getLogger(Initializer.class);

    @Autowired
    private TwitterStreamConsumer twitterStreamConsumer;

    /**
     * This event listener is invoked on the Spring context initialization
     * which is basically the application startup.
     */
    @EventListener(ContextRefreshedEvent.class)
    public void handleContextRefresh() {
        LOG.info("Starting the Twitter consumer : " + twitterStreamConsumer);

        try {
            twitterStreamConsumer.run();
        } catch (InterruptedException e) {
            LOG.warn("The twitterStreamConsumer.run() has been interrupted.", e);
        }
    }
}
