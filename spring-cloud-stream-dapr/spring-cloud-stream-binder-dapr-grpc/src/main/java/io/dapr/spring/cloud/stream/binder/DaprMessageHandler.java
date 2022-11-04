/*
 * Copyright 2022 The Dapr Authors
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dapr.spring.cloud.stream.binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.handler.AbstractMessageProducingHandler;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

import io.dapr.client.DaprClient;
import io.dapr.client.domain.PublishEventRequest;

public class DaprMessageHandler extends AbstractMessageProducingHandler {
	private static Logger logger = LoggerFactory.getLogger(DaprMessageHandler.class);

	private final String topic;
	private final String pubsubName;
    private final DaprClient daprClient;

	/**
    * Construct a {@link DaprMessageHandler} with the specified topic and pubsubName.
    *
    * @param topic the topic
    * @param pubsubName the pubsub name
    */
   public DaprMessageHandler(String topic, String pubsubName, DaprClient daprClient) {
       Assert.hasText(topic, "topic can't be null or empty");
       Assert.hasText(pubsubName, "pubsubName can't be null or empty");
       this.topic = topic;
       this.pubsubName = pubsubName;
       this.daprClient = daprClient;
   }

    @Override
    protected void handleMessageInternal(Message<?> message) {
        PublishEventRequest request = new PublishEventRequest(pubsubName, topic, message);
        this.daprClient.publishEvent(request).block();
        logger.info("succeed to send event " + message + "to " + pubsubName + "/"  +  topic);
    }
    
}
