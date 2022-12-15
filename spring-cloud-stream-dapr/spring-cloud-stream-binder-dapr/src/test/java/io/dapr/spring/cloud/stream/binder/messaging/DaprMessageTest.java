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

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;

import org.junit.jupiter.api.Test;

public class DaprMessageTest {

    @Test
    public void testDaprMessage() {
        byte[] payload = "payload".getBytes();
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("key", "value");
        DaprMessage daprMessage = new DaprMessage(payload, headers);

        Assert.assertEquals(daprMessage.getHeaders(), headers);
        Assert.assertEquals(daprMessage.getPayload(), payload);

        byte[] fakePayload = "fake-payload".getBytes();
        daprMessage.setPayload(fakePayload);
        Map<String, Object> fakeHeaders = new HashMap<String, Object>();
        fakeHeaders.put("fake-key", "fake-value");
        daprMessage.setHeaders(fakeHeaders);

        Assert.assertEquals(daprMessage.getHeaders(), fakeHeaders);
        Assert.assertEquals(daprMessage.getPayload(), fakePayload);
    }

}