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

package io.dapr.spring.cloud.stream.binder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.ArgumentCaptor;

import io.dapr.serializer.DaprObjectSerializer;
import io.dapr.serializer.DefaultObjectSerializer;
import io.dapr.spring.cloud.stream.binder.messaging.DaprMessageConverter;
import io.dapr.v1.DaprAppCallbackProtos;
import io.grpc.stub.StreamObserver;

public class DaprMessageProducerTest {

    /**
     * The purpose of this test is to show if the response when GRPC event is called
     * is reasonable.
     * It can indicate subscribing message from Dapr sidecar is successful.
     */
    @Test
    public void testSubscribe() {
        String pubsubName = "pubsubName";
        String topic = "topic";
        DaprObjectSerializer objectSerializer = new DefaultObjectSerializer();
        DaprMessageConverter daprMessageConverter = new DaprMessageConverter(objectSerializer);
        DaprGrpcService daprGrpcServic = new DaprGrpcService();

        DaprMessageProducer producer = new DaprMessageProducer(daprGrpcServic, daprMessageConverter, pubsubName, topic);
        DaprAppCallbackProtos.TopicEventRequest request = DaprAppCallbackProtos.TopicEventRequest.newBuilder().build();
        StreamObserver<DaprAppCallbackProtos.TopicEventResponse> responseObserver = mock(StreamObserver.class);
        ArgumentCaptor<DaprAppCallbackProtos.TopicEventResponse> captor = ArgumentCaptor
                .forClass(DaprAppCallbackProtos.TopicEventResponse.class);

        daprGrpcServic.onTopicEvent(request, responseObserver);

        verify(responseObserver, times(1)).onNext(captor.capture());
        DaprAppCallbackProtos.TopicEventResponse response = captor.getValue();
        Assert.assertEquals(response.getStatus(),
                DaprAppCallbackProtos.TopicEventResponse.TopicEventResponseStatus.SUCCESS);
        verify(responseObserver, times(1)).onCompleted();
    }
}