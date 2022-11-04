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

package io.dapr.spring.cloud.stream.binder.messaging;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.cloud.stream.converter.ConversionException;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;

import io.dapr.v1.DaprAppCallbackProtos;
import io.dapr.v1.DaprProtos;

/**
 * A converter to turn a {@link Message} to
 * {@link DaprProtos.PublishEventRequest.Builder}.
 * and turn a {@link DaprAppCallbackProtos.TopicEventRequest} to
 * {@link Message}.
 */
public class DaprMessageConverter
		implements DaprConverter<DaprProtos.PublishEventRequest.Builder, DaprAppCallbackProtos.TopicEventRequest> {

	private static Logger logger = LoggerFactory.getLogger(DaprMessageConverter.class);

	private static final Set<String> SUPPORT_MESSAGE_HEADERS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
			DaprHeaders.RAW_PAYLOAD,
			DaprHeaders.TTL_IN_SECONDS)));
	private final ObjectMapper objectMapper;

	public DaprMessageConverter() {
		objectMapper = new ObjectMapper();
	}

	@Override
	public DaprProtos.PublishEventRequest.Builder fromMessage(Message<?> message) {
		Map<String, Object> copyHeaders = new HashMap<String, Object>(message.getHeaders());
		DaprProtos.PublishEventRequest.Builder builder = DaprProtos.PublishEventRequest.newBuilder();
		DaprMessage daprMessage = new DaprMessage((byte[]) message.getPayload(), message.getHeaders());
		String stringMessage;
		try {
			stringMessage = objectMapper.writeValueAsString(daprMessage);
		} catch (JsonProcessingException e) {
			throw new ConversionException("Failed to convert Dapr message to json string:" + e);
		}
		ByteString byteString = ByteString.copyFrom(stringMessage, Charset.defaultCharset());
		builder.setData(byteString);
		PropertyMapper propertyMapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
		propertyMapper.from((String) copyHeaders.remove(DaprHeaders.CONTENT_TYPE)).to(builder::setDataContentType);
		propertyMapper.from((Map<String, String>) copyHeaders.remove(DaprHeaders.SPECIFIED_BROKER_METADATA))
				.to(builder::putAllMetadata);
		SUPPORT_MESSAGE_HEADERS.forEach(key -> propertyMapper.from(copyHeaders.remove(key))
				.to(value -> builder.putMetadata(key, value.toString())));
		return builder;
	}

	@Override
	public Message<?> toMessage(DaprAppCallbackProtos.TopicEventRequest topicEventRequest) {
		DaprMessage daprMessage = new DaprMessage();
		try {
			daprMessage = objectMapper.readValue(new String(topicEventRequest.getData().toByteArray()),
					DaprMessage.class);
		} catch (Exception e) {
			// throw new ConversionException("Failed to convert json string to Dapr
			// message:" + e);
			logger.error("Fail to convert Message, forward directly");
			return MessageBuilder.withPayload(topicEventRequest.getData().toByteArray()).build();
		}
		return MessageBuilder.withPayload(daprMessage.getPayload()).copyHeaders(daprMessage.getHeaders()).build();
	}
}
