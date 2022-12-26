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

package io.dapr.spring.cloud.stream.binder.config;

import io.dapr.client.DaprClient;
import io.dapr.spring.cloud.stream.binder.DaprGrpcService;
import io.dapr.spring.cloud.stream.binder.DaprMessageChannelBinder;
import io.dapr.spring.cloud.stream.binder.messaging.DaprMessageConverter;
import io.dapr.spring.cloud.stream.binder.properties.DaprBinderConfigurationProperties;
import io.dapr.spring.cloud.stream.binder.properties.DaprExtendedBindingProperties;
import io.dapr.spring.cloud.stream.binder.provisioning.DaprBinderProvisioner;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cloud.stream.binder.Binder;

import static org.mockito.Mockito.mock;

public class DaprBinderConfigurationTest {

  private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
      .withConfiguration(AutoConfigurations.of(DaprBinderConfiguration.class));

  @Test
  public void testConfiguration_whenBinderBeanExist() {
    this.contextRunner.withBean(Binder.class, () -> mock(Binder.class)).run(context -> {
      try {
        context.getBean(DaprBinderConfiguration.class);
        Assert.fail();
      } catch (Exception e) {
        // must throw an NoSuchBean exception
        Assert.assertEquals(e.getMessage(), 
            "No qualifying bean of type 'io.dapr.spring.cloud.stream.binder.config.DaprBinderConfiguration' available");
      }

      try {
        Assert.assertNull(context.getBean(DaprMessageChannelBinder.class));
        Assert.fail();
      } catch (Exception e) {
        // must throw an NoSuchBean exception
        Assert.assertEquals(e.getMessage(), 
            "No qualifying bean of type 'io.dapr.spring.cloud.stream.binder.DaprMessageChannelBinder' available");
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

}
