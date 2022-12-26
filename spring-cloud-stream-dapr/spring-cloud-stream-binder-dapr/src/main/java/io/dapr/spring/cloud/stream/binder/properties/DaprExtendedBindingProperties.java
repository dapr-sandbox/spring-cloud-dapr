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

package io.dapr.spring.cloud.stream.binder.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.binder.AbstractExtendedBindingProperties;
import org.springframework.cloud.stream.binder.BinderSpecificPropertiesProvider;

import java.util.Map;

/**
 * The extended Dapr binding configuration properties.
 */
@ConfigurationProperties(prefix = "spring.cloud.stream.dapr")
public class DaprExtendedBindingProperties
    extends
    AbstractExtendedBindingProperties<DaprConsumerProperties, DaprProducerProperties, DaprBindingProperties> {

  private static final String DEFAULTS_PREFIX = "spring.cloud.stream.dapr.default";

  @Override
  public Class<? extends BinderSpecificPropertiesProvider> getExtendedPropertiesEntryClass() {
    return DaprBindingProperties.class;
  }

  @Override
  public Map<String, DaprBindingProperties> getBindings() {
    return this.doGetBindings();
  }

  @Override
  public String getDefaultsPrefix() {
    return DEFAULTS_PREFIX;
  }
}
