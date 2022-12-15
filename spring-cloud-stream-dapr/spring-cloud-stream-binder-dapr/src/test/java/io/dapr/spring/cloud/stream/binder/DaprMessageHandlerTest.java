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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.support.GenericMessage;

import io.dapr.client.DaprClient;
import io.dapr.client.domain.PublishEventRequest;
import io.dapr.serializer.DaprObjectSerializer;
import io.dapr.serializer.DefaultObjectSerializer;
import io.dapr.spring.cloud.stream.binder.messaging.DaprMessageConverter;
import reactor.core.publisher.Mono;

public class DaprMessageHandlerTest {

	/**
	 * The purpose of this test is to show if the method publishEvent() of Dapr
	 * Java SDK runs successfully when message handler is called.
	 * It can indicate publishing message to Dapr sidecar is successful.
	 */
	@Test
	public void testPublish() {
		Map<String, Object> valueMap = new HashMap<>(2);
		valueMap.put("key1", "value1");
		valueMap.put("key2", "value2");
		Message<?> message = new GenericMessage<>("testPayload".getBytes(), valueMap);

		String topic = "topic";
		String pubsubName = "pubsub";
		DaprClient daprClient = mock(DaprClient.class);
		DaprObjectSerializer objectSerializer = new DefaultObjectSerializer();
		DaprMessageConverter converter = new DaprMessageConverter(objectSerializer);
		DaprMessageHandler daprMessageHandler = new DaprMessageHandler(converter, topic, pubsubName,
				daprClient);

		when(daprClient.publishEvent(any())).thenReturn(Mono.empty());
		daprMessageHandler.handleMessage(message);

		verify(daprClient, times(1)).publishEvent(isA(PublishEventRequest.class));
	}
}