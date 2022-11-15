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

import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.messaging.Message;

import io.dapr.spring.cloud.stream.binder.messaging.DaprMessageConverter;
import io.dapr.v1.DaprAppCallbackProtos;

/**
 * Class that deals with messages from TopicEvent in DaprGrpcService.
 */
public class DaprMessageProducer extends MessageProducerSupport {
	private final DaprGrpcService daprGrpcService;
	private final DaprMessageConverter daprMessageConverter;

	public DaprMessageProducer(DaprGrpcService daprGrpcService,
			DaprMessageConverter daprMessageConverter,
			String pubsubName,
			String topic) {
		this.daprMessageConverter = daprMessageConverter;
		this.daprGrpcService = daprGrpcService;
		this.daprGrpcService.registerConsumer(pubsubName, topic,
				new DaprMessageConsumer(pubsubName, topic, this::onMessage));
	}

	private void onMessage(DaprAppCallbackProtos.TopicEventRequest request) {
		Message<?> message = daprMessageConverter.toMessage(request);
		sendMessage(message);
	}

}
