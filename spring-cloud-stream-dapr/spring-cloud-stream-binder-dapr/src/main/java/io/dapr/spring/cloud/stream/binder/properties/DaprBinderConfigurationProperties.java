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
import io.dapr.config.Properties;
import io.dapr.client.DaprApiProtocol;

/**
 * Configuration properties for the Dapr binder.
 * 
 * The properties in this class are prefixed with
 * <b>spring.cloud.stream.dapr.binder</b>.
 */
@ConfigurationProperties(prefix = "spring.cloud.stream.dapr.binder")
public class DaprBinderConfigurationProperties extends Properties {
	public String getAddress() {
		return SIDECAR_IP.get();
	}

	public void setAddress(String address) {
		System.getProperties().setProperty(SIDECAR_IP.getName(), address);
	}

	public int getGrpcPort() {
		return GRPC_PORT.get();
	}

	public void setGrpcPort(int grpcPort) {
		System.getProperties().setProperty(GRPC_PORT.getName(), String.valueOf(grpcPort));
	}

	public void switchToGRPC() {
		System.getProperties().setProperty(Properties.API_PROTOCOL.getName(), DaprApiProtocol.GRPC.name());
		System.getProperties().setProperty(
				Properties.API_METHOD_INVOCATION_PROTOCOL.getName(),
				DaprApiProtocol.GRPC.name());
	}

}
