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

/**
 * Dapr internal headers for spring messaging messages.
 */
public final class DaprHeaders {

	private DaprHeaders() {
	}

	/**
	 * The contentType tells Dapr which content type your data adheres to when
	 * constructing a CloudEvent envelope.
	 *
	 * <p>
	 * Type: String
	 */
	public static final String CONTENT_TYPE = "contentType";

	/**
	 * The number of seconds for the message to expire.
	 *
	 * <p>
	 * Spring message header type: Long
	 */
	public static final String TTL_IN_SECONDS = "ttlInSeconds";

	/**
	 * Determine if Dapr should publish the event without wrapping it as
	 * CloudEvent.Not using CloudEvents disables support for tracing, event
	 * deduplication per messageId, content-type metadata, and any other features
	 * built using the CloudEvent schema.
	 *
	 * <p>
	 * Message Header Type: Boolean
	 */
	public static final String RAW_PAYLOAD = "rawPayload";

	/**
	 * Some metadata parameters are available based on each pubsub broker component.
	 *
	 * <p>
	 * Spring message header type: Map&lt;string, string&gt;
	 */
	public static final String SPECIFIED_BROKER_METADATA = "specifiedBrokerMetadata";
}
