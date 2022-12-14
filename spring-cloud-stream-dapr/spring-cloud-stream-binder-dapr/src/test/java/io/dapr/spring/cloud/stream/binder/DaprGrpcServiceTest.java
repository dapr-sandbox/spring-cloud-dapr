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

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Assert;
import org.mockito.ArgumentCaptor;

import io.dapr.serializer.DaprObjectSerializer;
import io.dapr.serializer.DefaultObjectSerializer;
import io.dapr.spring.cloud.stream.binder.messaging.DaprMessageConverter;
import io.dapr.v1.DaprAppCallbackProtos;
import io.dapr.v1.DaprAppCallbackProtos.TopicSubscription;
import io.grpc.stub.StreamObserver;
import com.google.protobuf.Empty;

public class DaprGrpcServiceTest {
    private String pubsubName;
    private String topic;
    private DaprObjectSerializer objectSerializer;
    private DaprMessageConverter daprMessageConverter;
    private DaprGrpcService daprGrpcServic;
    private DaprMessageProducer producer;

    @BeforeEach
    public void beforeEach() {
        pubsubName = "pubsubName";
        topic = "topic";
        objectSerializer = new DefaultObjectSerializer();
        daprMessageConverter = new DaprMessageConverter(objectSerializer);
        daprGrpcServic = new DaprGrpcService();
        producer = new DaprMessageProducer(daprGrpcServic, daprMessageConverter, pubsubName, topic);
    }

    /**
     * The purpose of this test is to show if the response when GRPC event is called
     * is reasonable.
     * It can indicate subscribing message from Dapr sidecar is successful.
     */
    @Test
    public void testOnTopicEvent() {
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

    /**
     * This test is to show if the subscription information can be sent successfully
     * via GRPC.
     */
    @Test
    public void testListTopicSubscriptionst() {
        Empty request = Empty.newBuilder().build();
        StreamObserver<DaprAppCallbackProtos.ListTopicSubscriptionsResponse> responseObserver = mock(
                StreamObserver.class);
        ArgumentCaptor<DaprAppCallbackProtos.ListTopicSubscriptionsResponse> captor = ArgumentCaptor
                .forClass(DaprAppCallbackProtos.ListTopicSubscriptionsResponse.class);

        daprGrpcServic.listTopicSubscriptions(request, responseObserver);

        verify(responseObserver, times(1)).onNext(captor.capture());
        DaprAppCallbackProtos.ListTopicSubscriptionsResponse response = captor.getValue();
        List<TopicSubscription> list = response.getSubscriptionsList();

        Assert.assertEquals(list.size(), 1);
        Assert.assertEquals(list.get(0).getPubsubName(), "pubsubName");
        Assert.assertEquals(list.get(0).getTopic(), "topic");
        verify(responseObserver, times(1)).onCompleted();
    }
}