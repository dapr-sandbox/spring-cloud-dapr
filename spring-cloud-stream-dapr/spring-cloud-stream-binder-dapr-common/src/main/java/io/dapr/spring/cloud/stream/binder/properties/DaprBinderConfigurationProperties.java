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

package io.dapr.spring.cloud.stream.binder.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Dapr binder.
 * 
 * The properties in this class are prefixed with
 * <b>spring.cloud.stream.dapr.binder</b>.
 */
@ConfigurationProperties(prefix = "spring.cloud.stream.dapr.binder")
public class DaprBinderConfigurationProperties {
	/**
	 * Dapr's sidecar address.
	 */
	private String address = "127.0.0.1";

	/**
	 * Dapr's HTTP port.
	 */
	private int httpPort = 3501;

	/**
	 * Dapr's gRPC port.
	 */
	private int grpcPort = 50001;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public int getGrpcPort() {
		return grpcPort;
	}

	public void setGrpcPort(int grpcPort) {
		this.grpcPort = grpcPort;
	}
}
