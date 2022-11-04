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

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;

/**
 * A converter to turn a {@link Message} from serialized form to a typed Object
 * and vice versa.
 *
 * @param <I> The Dapr message type when sending to the broker using Dapr SDK.
 * @param <O> The Dapr message type when receiving from the broker using Dapr
 *            SDK.
 */
public interface DaprConverter<I, O> {

	/**
	 * Convert a {@link Message} from a serialized form to a typed Object.
	 *
	 * @param message the input message
	 * @return the result of the conversion, or {@code null} if the converter cannot
	 *         perform the conversion.
	 */
	@Nullable
	I fromMessage(Message<?> message) throws JsonProcessingException;

	/**
	 * Create a {@link Message} whose payload is the result of converting the given
	 * payload Object to serialized form.
	 * 
	 * @param daprMessage the Object to convert
	 * @return the new message, or {@code null} if the converter does not support
	 *         the
	 *         Object type or the target media type
	 */
	@Nullable
	Message<?> toMessage(O daprMessage) throws IOException;
}
