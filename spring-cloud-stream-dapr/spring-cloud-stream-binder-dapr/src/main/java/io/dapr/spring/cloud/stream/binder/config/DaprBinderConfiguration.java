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

package io.dapr.spring.cloud.stream.binder.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.binder.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.serializer.DaprObjectSerializer;
import io.dapr.serializer.DefaultObjectSerializer;
import io.dapr.spring.cloud.stream.binder.DaprGrpcService;
import io.dapr.spring.cloud.stream.binder.DaprMessageChannelBinder;
import io.dapr.spring.cloud.stream.binder.messaging.DaprMessageConverter;
import io.dapr.spring.cloud.stream.binder.properties.DaprBinderConfigurationProperties;
import io.dapr.spring.cloud.stream.binder.properties.DaprExtendedBindingProperties;
import io.dapr.spring.cloud.stream.binder.provisioning.DaprBinderProvisioner;

/**
 * Dapr binder's Spring Boot AutoConfiguration.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(Binder.class)
@EnableConfigurationProperties({ DaprBinderConfigurationProperties.class, DaprExtendedBindingProperties.class })
public class DaprBinderConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public DaprBinderProvisioner daprBinderProvisioner() {
		return new DaprBinderProvisioner();
	}

	/**
	 * Implement different DaprObjectSerializer interfaces to support multiple ways to serialize
	 * Currently using DaprObjectSerializer temporarily
	 */
	@Bean
	@ConditionalOnMissingBean
	public DaprObjectSerializer daprObjectSerializer() {
		return new DefaultObjectSerializer();
	}

	@Bean
	@ConditionalOnMissingBean
	public DaprMessageConverter daprMessageConverter(DaprObjectSerializer daprObjectSerializer) {
		return new DaprMessageConverter(daprObjectSerializer);
	}

	@Bean
	@ConditionalOnMissingBean
	public DaprMessageChannelBinder daprMessageChannelBinder(DaprBinderProvisioner daprBinderProvisioner,
			DaprExtendedBindingProperties daprExtendedBindingProperties, DaprClient daprClient,
			DaprGrpcService daprGrpcService,
			DaprMessageConverter daprMessageConverter) {
		return new DaprMessageChannelBinder(
				null,
				daprBinderProvisioner,
				daprExtendedBindingProperties,
				daprClient, daprGrpcService, daprMessageConverter);
	}

	@Bean
	@ConditionalOnMissingBean
	public DaprClient daprClient(DaprBinderConfigurationProperties properties) {

		// switch procotol to GRPC
		properties.switchToGRPC();

		// build grpc client
		DaprClient client = new DaprClientBuilder()
				.build();
		return client;
	}
}
