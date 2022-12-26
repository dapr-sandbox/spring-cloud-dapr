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

package io.dapr.spring.cloud.stream.binder;

import io.dapr.spring.cloud.stream.binder.config.DaprBinderConfiguration;
import io.dapr.spring.cloud.stream.binder.properties.DaprConsumerProperties;
import io.dapr.spring.cloud.stream.binder.properties.DaprProducerProperties;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.mockito.Mockito.mock;

public class DaprMessageChannelBinderTest {
  private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
      .withConfiguration(AutoConfigurations.of(DaprBinderConfiguration.class));

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
          String defaultsPrefix = binder.getDefaultsPrefix();

          Assert.assertEquals(producerProperties.getPubsubName(), "producer-fake-pubsub-name");
          Assert.assertEquals(consumerProperties.getPubsubName(), "consumer-fake-pubsub-name");
          Assert.assertEquals(defaultsPrefix, "spring.cloud.stream.dapr.default");
          Assert.assertNotNull(binder.getExtendedPropertiesEntryClass());
        });
  }
}
