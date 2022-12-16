package io.dapr.spring.cloud.stream.binder.config;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import io.dapr.spring.cloud.stream.binder.DaprGrpcService;

public class DaprGrpcServerAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(DaprGrpcServerAutoConfiguration.class));

    @Test
    public void testConfiguration_default() {
        this.contextRunner.run(context -> {
            Assert.assertNotNull(context.getBean(DaprGrpcService.class));
        });
    }

}
