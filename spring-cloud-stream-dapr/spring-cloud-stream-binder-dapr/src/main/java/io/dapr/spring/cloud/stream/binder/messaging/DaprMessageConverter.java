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
limitations under the License.
*/

package io.dapr.spring.cloud.stream.binder.messaging;

import io.dapr.client.domain.PublishEventRequest;
import io.dapr.serializer.DaprObjectSerializer;
import io.dapr.utils.TypeRef;
import io.dapr.v1.DaprAppCallbackProtos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * Implementation of DaprConverter.@see {@link DaprConverter}.
 */
public class DaprMessageConverter
    implements DaprConverter<PublishEventRequest, DaprAppCallbackProtos.TopicEventRequest> {

  private static Logger logger = LoggerFactory.getLogger(DaprMessageConverter.class);

  private final DaprObjectSerializer objectSerializer;

  public DaprMessageConverter(DaprObjectSerializer objectSerializer) {
    this.objectSerializer = objectSerializer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PublishEventRequest fromMessage(Message<?> message, String pubsubName, String topic) {
    return new PublishEventRequest(pubsubName, topic, message);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Message<?> toMessage(DaprAppCallbackProtos.TopicEventRequest topicEventRequest) {
    DaprMessage daprMessage = new DaprMessage();
    try {
      daprMessage = objectSerializer.deserialize(topicEventRequest.getData().toByteArray(),
          new TypeRef<DaprMessage>() {
          });
    } catch (Exception e) {
      logger.error("Fail to convert Message, forward directly");
      return MessageBuilder.withPayload(topicEventRequest.getData().toByteArray()).build();
    }
    return MessageBuilder.withPayload(daprMessage.getPayload()).copyHeaders(daprMessage.getHeaders()).build();
  }
}
