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

import com.google.protobuf.ByteString;
import io.dapr.client.domain.PublishEventRequest;
import io.dapr.serializer.DaprObjectSerializer;
import io.dapr.serializer.DefaultObjectSerializer;
import io.dapr.v1.DaprAppCallbackProtos.TopicEventRequest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Map;

public class DaprMessageConverterTest {

  @Test
  public void testFromMessage() {
    Map<String, String> brokerMetadata = new HashMap<>();
    brokerMetadata.put("partitionKey", "fake-partiton-key");
    Message<?> message = MessageBuilder.withPayload("testPayload".getBytes())
        .setHeader("contentType", "fake-content-type")
        .setHeader("rawPayload", true)
        .setHeader("ttlInSeconds", 20)
        .setHeader("specifiedBrokerMetadata", brokerMetadata).build();
    DaprObjectSerializer objectSerializer = new DefaultObjectSerializer();
    DaprMessageConverter converter = new DaprMessageConverter(objectSerializer);
    String topic = "fake-topic";
    String pubsubName = "fake-pubsub-name";

    PublishEventRequest repuest = converter.fromMessage(message, pubsubName, topic);
    Assert.assertEquals(repuest.getPubsubName(), "fake-pubsub-name");
    Assert.assertEquals(repuest.getTopic(), "fake-topic");
    Message<?> tmpMessage = (Message<?>) repuest.getData();
    Assert.assertEquals(tmpMessage.getHeaders().get("contentType"), "fake-content-type");
    Assert.assertEquals(tmpMessage.getHeaders().get("rawPayload"), true);
    Assert.assertEquals(tmpMessage.getHeaders().get("ttlInSeconds"), 20);
    Assert.assertEquals(tmpMessage.getHeaders().get("specifiedBrokerMetadata"), brokerMetadata);
  }

  @Test
  public void testToMessage() {
    String data = 
        "{\"headers\":{\"contentType\":\"application/json\",\"key\":\"value\"},\"payload\":\"ZmFrZSBwYXlsb2Fk\"}";
    TopicEventRequest request = TopicEventRequest.newBuilder().setData(ByteString.copyFrom(data.getBytes()))
        .build();
    DaprObjectSerializer objectSerializer = new DefaultObjectSerializer();
    DaprMessageConverter converter = new DaprMessageConverter(objectSerializer);

    Message<?> result = converter.toMessage(request);
    Assert.assertEquals(result.getHeaders().get("contentType"), "application/json");
    Assert.assertEquals(result.getHeaders().get("key"), "value");
    Assert.assertArrayEquals((byte[]) result.getPayload(), "fake payload".getBytes());
  }

}