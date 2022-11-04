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

import java.io.Serializable;
import java.util.Map;

import org.springframework.messaging.Message;

/**
 * Encapsulates {@link Message} payload and headers for serialization.
 */
public class DaprMessage implements Serializable {

	byte[] payload;

	Map<String, Object> headers;

	DaprMessage() {
	}

	public DaprMessage(byte[] payload, Map<String, Object> headers) {
		this.payload = payload;
		this.headers = headers;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}
}
