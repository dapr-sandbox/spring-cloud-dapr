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

import org.springframework.cloud.stream.binder.AbstractMessageChannelBinder;
import org.springframework.cloud.stream.binder.BinderSpecificPropertiesProvider;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.binder.ExtendedPropertiesBinder;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.integration.core.MessageProducer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import io.dapr.client.DaprClient;
import io.dapr.spring.cloud.stream.binder.messaging.DaprMessageConverter;
import io.dapr.spring.cloud.stream.binder.properties.DaprConsumerProperties;
import io.dapr.spring.cloud.stream.binder.properties.DaprExtendedBindingProperties;
import io.dapr.spring.cloud.stream.binder.properties.DaprProducerProperties;
import io.dapr.spring.cloud.stream.binder.provisioning.DaprBinderProvisioner;

/**
 * A {@link io.dapr.spring.cloud.stream.binder} that uses Dapr sidecar as the
 * underlying middleware.
 */
public class DaprMessageChannelBinder extends
        AbstractMessageChannelBinder<ExtendedConsumerProperties<DaprConsumerProperties>, ExtendedProducerProperties<DaprProducerProperties>, DaprBinderProvisioner>
        implements
        ExtendedPropertiesBinder<MessageChannel, DaprConsumerProperties, DaprProducerProperties> {

    private final DaprExtendedBindingProperties bindingProperties;
    private final DaprClient daprClient;

    private final DaprGrpcService daprGrpcService;
    private final DaprMessageConverter daprMessageConverter;

    public DaprMessageChannelBinder(String[] headersToEmbed,
            DaprBinderProvisioner provisioningProvider,
            DaprExtendedBindingProperties bindingProperties,
            DaprClient daprClient, DaprGrpcService daprGrpcService, DaprMessageConverter daprMessageConverter) {
        super(headersToEmbed, provisioningProvider);
        this.bindingProperties = bindingProperties;
        this.daprClient = daprClient;
        this.daprGrpcService = daprGrpcService;
        this.daprMessageConverter = daprMessageConverter;
    }

    @Override
    public DaprConsumerProperties getExtendedConsumerProperties(String channelName) {
        return this.bindingProperties.getExtendedConsumerProperties(channelName);
    }

    @Override
    public DaprProducerProperties getExtendedProducerProperties(String channelName) {
        return this.bindingProperties.getExtendedProducerProperties(channelName);
    }

    @Override
    public String getDefaultsPrefix() {
        return this.bindingProperties.getDefaultsPrefix();
    }

    @Override
    public Class<? extends BinderSpecificPropertiesProvider> getExtendedPropertiesEntryClass() {
        return this.bindingProperties.getExtendedPropertiesEntryClass();
    }

    @Override
    protected MessageHandler createProducerMessageHandler(ProducerDestination destination,
            ExtendedProducerProperties<DaprProducerProperties> producerProperties,
            MessageChannel errorChannel)
            throws Exception {
        DaprProducerProperties extension = producerProperties.getExtension();
        DaprMessageHandler daprMessageHandler = new DaprMessageHandler(
                daprMessageConverter, destination.getName(), extension.getPubsubName(), daprClient);
        daprMessageHandler.setBeanFactory(getBeanFactory());
        return daprMessageHandler;
    }

    @Override
    protected MessageProducer createConsumerEndpoint(ConsumerDestination destination, String group,
            ExtendedConsumerProperties<DaprConsumerProperties> properties) throws Exception {
        return new DaprMessageProducer(daprGrpcService,
                daprMessageConverter, properties.getExtension().getPubsubName(),
                destination.getName());
    }

}
