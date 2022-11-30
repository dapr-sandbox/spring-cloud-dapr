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

import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cloud.stream.binder.Binder;

import io.dapr.client.DaprClient;
import io.dapr.spring.cloud.stream.binder.DaprGrpcService;
import io.dapr.spring.cloud.stream.binder.DaprMessageChannelBinder;
import io.dapr.spring.cloud.stream.binder.messaging.DaprMessageConverter;
import io.dapr.spring.cloud.stream.binder.properties.DaprBinderConfigurationProperties;
import io.dapr.spring.cloud.stream.binder.properties.DaprConsumerProperties;
import io.dapr.spring.cloud.stream.binder.properties.DaprExtendedBindingProperties;
import io.dapr.spring.cloud.stream.binder.properties.DaprProducerProperties;
import io.dapr.spring.cloud.stream.binder.provisioning.DaprBinderProvisioner;

public class DaprBinderConfigurationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(DaprBinderConfiguration.class));

	@Test
	public void testConfiguration_whenBinderBeanExist() {
		this.contextRunner.withBean(Binder.class, () -> mock(Binder.class))
				.run(context -> {
					try {
						context.getBean(DaprBinderConfiguration.class);
					} catch (NoSuchBeanDefinitionException e) {
						Assert.assertTrue(true);
					} catch (Exception e) {
						Assert.assertTrue(false);
					}

					try {
						Assert.assertNull(context.getBean(DaprMessageChannelBinder.class));
					} catch (NoSuchBeanDefinitionException e) {
						Assert.assertTrue(true);
					} catch (Exception e) {
						Assert.assertTrue(false);
					}
				});
	}

	@Test
	public void testConfiguration_default() {
		this.contextRunner.withBean(DaprGrpcService.class, () -> mock(DaprGrpcService.class))
				.run(context -> {
					Assert.assertNotNull(context.getBean(DaprBinderConfiguration.class));
					Assert.assertNotNull(context.getBean(DaprBinderProvisioner.class));
					Assert.assertNotNull(context.getBean(DaprMessageConverter.class));
					Assert.assertNotNull(context.getBean(DaprMessageChannelBinder.class));
					Assert.assertNotNull(context.getBean(DaprClient.class));
					Assert.assertNotNull(context.getBean(DaprExtendedBindingProperties.class));
					Assert.assertNotNull(context.getBean(DaprBinderConfigurationProperties.class));
				});
	}

	@Test
	public void testExtendedBindingProperties() {
		this.contextRunner.withBean(DaprGrpcService.class, () -> mock(DaprGrpcService.class))
				.withPropertyValues(
						"spring.cloud.stream.dapr.bindings.input.producer.pubsub-name=producer-fake-pubsub-name",
						"spring.cloud.stream.dapr.bindings.output.consumer.pubsub-name=consumer-fake-pubsub-name")
				.run(context -> {
					Assert.assertNotNull(context.getBean(DaprMessageChannelBinder.class));
					DaprMessageChannelBinder binder = context.getBean(DaprMessageChannelBinder.class);
					DaprProducerProperties producerProperties = binder.getExtendedProducerProperties("input");
					DaprConsumerProperties consumerProperties = binder.getExtendedConsumerProperties("output");
					
					Assert.assertEquals(producerProperties.getPubsubName(), "producer-fake-pubsub-name");
					Assert.assertEquals(consumerProperties.getPubsubName(), "consumer-fake-pubsub-name");
				});
	}
}
