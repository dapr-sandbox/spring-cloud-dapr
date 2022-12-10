package io.dapr.spring.cloud.stream.binder.provision;

import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import io.dapr.spring.cloud.stream.binder.DaprGrpcService;
import io.dapr.spring.cloud.stream.binder.DaprMessageChannelBinder;
import io.dapr.spring.cloud.stream.binder.config.DaprBinderConfiguration;
import io.dapr.spring.cloud.stream.binder.properties.DaprConsumerProperties;
import io.dapr.spring.cloud.stream.binder.properties.DaprProducerProperties;

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
