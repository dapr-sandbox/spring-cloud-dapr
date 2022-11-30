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

import org.junit.Test;

import io.dapr.client.DaprApiProtocol;

import org.junit.Assert;
import org.junit.Before;

public class DaprBinderConfigurationPropertiesTest {

	private DaprBinderConfigurationProperties binderProperties;

	@Before
	public void beforeEach() {
		binderProperties = new DaprBinderConfigurationProperties();
	}

	@Test
	public void testGetAddress() {
		Assert.assertEquals(binderProperties.getAddress(),"127.0.0.1");
	}

	@Test
	public void testGetGrpcPort() {
		Assert.assertEquals(binderProperties.getGrpcPort(),50001);
	}

	@Test
	public void testSwitchToGRPC() {
		binderProperties.switchToGRPC();
		
		Assert.assertEquals(binderProperties.getApiProtecol(),DaprApiProtocol.GRPC);
		Assert.assertEquals(binderProperties.getApiMethodInvocationProtecol(),DaprApiProtocol.GRPC);
	}
}
