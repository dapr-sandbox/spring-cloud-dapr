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

package io.dapr.spring.cloud.stream.sample.kafka;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

/**
 * The {@link SampleApplication} is main Application to run. Implement publish
 * and subscribe to message broker.
 */
@SpringBootApplication
public class SampleApplication {
	private static Logger logger = LoggerFactory.getLogger(SampleApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}

	private int i = 0;

	/**
	 * Generate messages for publish. 
	 * @return A method returning Message<T> to publish.
	 */
	@Bean
	public Supplier<Message<String>> supply() {
		return () -> {
			logger.info("Sending message, sequence " + i++);
			return MessageBuilder.withPayload("event body").build();
		};
	}

	/**
	 * Method to deal with suscribed messages.
	 */
	@Bean
	public Consumer<Message<String>> consume() {
		return message -> {
			logger.info("Message received : {}", message);
		};
	}

}
