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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.protobuf.Empty;
import io.dapr.v1.AppCallbackGrpc;
import io.dapr.v1.DaprAppCallbackProtos;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class DaprGrpcService extends AppCallbackGrpc.AppCallbackImplBase {
	private final List<DaprAppCallbackProtos.TopicSubscription> topicSubscriptionList = new ArrayList<>();
	private final List<Consumer<DaprAppCallbackProtos.TopicEventRequest>> consumers = new ArrayList<>();
	@Override
	public void listTopicSubscriptions(Empty request,
			StreamObserver<DaprAppCallbackProtos.ListTopicSubscriptionsResponse> responseObserver) {
		try {
			DaprAppCallbackProtos.ListTopicSubscriptionsResponse.Builder builder =
					DaprAppCallbackProtos.ListTopicSubscriptionsResponse.newBuilder();
			topicSubscriptionList.forEach(builder::addSubscriptions);
			DaprAppCallbackProtos.ListTopicSubscriptionsResponse response = builder.build();
			responseObserver.onNext(response);
		}
		catch (Throwable e) {
			responseObserver.onError(e);
		}
		finally {
			responseObserver.onCompleted();
		}
	}

	@Override
	public void onTopicEvent(DaprAppCallbackProtos.TopicEventRequest request,
			StreamObserver<DaprAppCallbackProtos.TopicEventResponse> responseObserver) {
		try {
			consumers.forEach(consumer -> consumer.accept(request));
			DaprAppCallbackProtos.TopicEventResponse response =
					DaprAppCallbackProtos.TopicEventResponse.newBuilder()
							.setStatus(DaprAppCallbackProtos.TopicEventResponse.TopicEventResponseStatus.SUCCESS)
							.build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		}
		catch (Throwable e) {
			responseObserver.onError(e);
		}
	}

	public void registerConsumer(String pubsubName, String topic, Consumer<DaprAppCallbackProtos.TopicEventRequest> consumer) {
		topicSubscriptionList.add(DaprAppCallbackProtos.TopicSubscription
				.newBuilder()
				.setPubsubName(pubsubName)
				.setTopic(topic)
				.build());
		consumers.add(consumer);
	}
}
