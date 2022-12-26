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

package io.dapr.spring.cloud.stream.binder.provision;

import io.dapr.spring.cloud.stream.binder.properties.DaprConsumerProperties;
import io.dapr.spring.cloud.stream.binder.properties.DaprProducerProperties;
import io.dapr.spring.cloud.stream.binder.provisioning.DaprBinderProvisioner;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;

public class DaprBinderProvisionerTest {
  private final DaprBinderProvisioner daprBinderProvisioner = new DaprBinderProvisioner();
  private final ExtendedProducerProperties<DaprProducerProperties> producerPproperties = null;
  private final ExtendedConsumerProperties<DaprConsumerProperties> consumerPproperties = null;

  @Test
  public void testProvisionProducerDestination() {
    ProducerDestination producerDestination = daprBinderProvisioner.provisionProducerDestination("topic",
        producerPproperties);
    Assert.assertNotNull(producerDestination);
    Assert.assertEquals(producerDestination.getName(), "topic");
    Assert.assertEquals(producerDestination.getNameForPartition(1), "topic-1");
  }

  @Test
  public void testProvisionConsumerDestination() {
    ConsumerDestination consumerDestination = daprBinderProvisioner.provisionConsumerDestination("topic", "group",
        consumerPproperties);
    Assert.assertNotNull(consumerDestination);
    Assert.assertEquals(consumerDestination.getName(), "topic");
  }
}