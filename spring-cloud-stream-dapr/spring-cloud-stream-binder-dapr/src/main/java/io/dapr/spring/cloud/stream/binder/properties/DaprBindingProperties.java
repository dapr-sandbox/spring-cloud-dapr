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

package io.dapr.spring.cloud.stream.binder.properties;

import org.springframework.cloud.stream.binder.BinderSpecificPropertiesProvider;

/**
 * Container object for Dapr specific extended producer and consumer binding
 * properties.
 */
public class DaprBindingProperties implements BinderSpecificPropertiesProvider {

	/**
	 * Consumer specific binding properties. @see {@link DaprConsumerProperties}.
	 */
	private DaprConsumerProperties consumer = new DaprConsumerProperties();

	/**
	 * Producer specific binding properties. @see {@link DaprProducerProperties}.
	 */
	private DaprProducerProperties producer = new DaprProducerProperties();

	public DaprConsumerProperties getConsumer() {
		return consumer;
	}

	public void setConsumer(DaprConsumerProperties consumer) {
		this.consumer = consumer;
	}

	public DaprProducerProperties getProducer() {
		return producer;
	}

	public void setProducer(DaprProducerProperties producer) {
		this.producer = producer;
	}
}
