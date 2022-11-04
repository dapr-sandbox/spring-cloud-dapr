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

import java.util.function.Consumer;

import io.dapr.v1.DaprAppCallbackProtos;

public class DaprMessageConsumer implements Consumer<DaprAppCallbackProtos.TopicEventRequest> {
	private final String topic;
	private final String pubsubName;
	private Consumer<DaprAppCallbackProtos.TopicEventRequest> integrationConsumer;

	public DaprMessageConsumer(String pubsubName, String topic,
			Consumer<DaprAppCallbackProtos.TopicEventRequest> integrationConsumer) {
		this.pubsubName = pubsubName;
		this.topic = topic;
		this.integrationConsumer = integrationConsumer;
	}

	@Override
	public void accept(DaprAppCallbackProtos.TopicEventRequest request) {
		if (this.topic.equals(request.getTopic()) && this.pubsubName.equals(request.getPubsubName())) {
			integrationConsumer.accept(request);
		}
	}
}
