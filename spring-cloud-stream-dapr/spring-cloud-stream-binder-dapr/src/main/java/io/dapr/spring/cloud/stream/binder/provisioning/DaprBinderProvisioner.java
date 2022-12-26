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

package io.dapr.spring.cloud.stream.binder.provisioning;

import io.dapr.spring.cloud.stream.binder.properties.DaprConsumerProperties;
import io.dapr.spring.cloud.stream.binder.properties.DaprProducerProperties;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.cloud.stream.provisioning.ProvisioningException;
import org.springframework.cloud.stream.provisioning.ProvisioningProvider;

/**
 * The {@link DaprBinderProvisioner} is responsible for the provisioning of
 * consumer and producer destinations.
 */
public class DaprBinderProvisioner
    implements ProvisioningProvider<ExtendedConsumerProperties<DaprConsumerProperties>, 
    ExtendedProducerProperties<DaprProducerProperties>> {

  @Override
  public ProducerDestination provisionProducerDestination(String name,
      ExtendedProducerProperties<DaprProducerProperties> properties) throws ProvisioningException {
    return new DaprProducerDestination(name);
  }

  @Override
  public ConsumerDestination provisionConsumerDestination(String name, String group,
      ExtendedConsumerProperties<DaprConsumerProperties> properties) throws ProvisioningException {
    return new DaprConsumerDestination(name);
  }

  private static final class DaprProducerDestination implements ProducerDestination {

    private final String topic;

    DaprProducerDestination(String topic) {
      this.topic = topic.trim();
    }

    @Override
    public String getName() {
      return topic;
    }

    @Override
    public String getNameForPartition(int partition) {
      return topic + "-" + partition;
    }
  }

  private static final class DaprConsumerDestination implements ConsumerDestination {

    private final String topic;

    DaprConsumerDestination(final String topic) {
      this.topic = topic.trim();
    }

    @Override
    public String getName() {
      return topic;
    }
  }
}
