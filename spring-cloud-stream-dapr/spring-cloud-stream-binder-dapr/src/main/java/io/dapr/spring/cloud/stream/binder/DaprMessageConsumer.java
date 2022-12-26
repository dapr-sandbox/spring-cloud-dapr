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
limitations under the License.
*/

package io.dapr.spring.cloud.stream.binder;

import io.dapr.v1.DaprAppCallbackProtos.TopicEventRequest;

import java.util.function.Consumer;

/**
 * Class that deals with messages in the way of Consumer method in main application. 
 */
public class DaprMessageConsumer implements Consumer<TopicEventRequest> {

  /**
   * Topic name to pub/sub.
   */
  private final String topic;

  /**
   * Pubsub name to pub/sub.
   */
  private final String pubsubName;

  /**
   * Method to process messages.
   */
  private Consumer<TopicEventRequest> integrationConsumer;

  /**
   * Constructor for DaprMessageConsumer.
   *
   * @param pubsubName Pubsub name to pub/sub..
   * @param topic Topic name to pub/sub.
   * @param integrationConsumer Method to process messages.
   */
  public DaprMessageConsumer(String pubsubName, String topic,
      Consumer<TopicEventRequest> integrationConsumer) {
    this.pubsubName = pubsubName;
    this.topic = topic;
    this.integrationConsumer = integrationConsumer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept(TopicEventRequest request) {
    if (this.topic.equals(request.getTopic()) && this.pubsubName.equals(request.getPubsubName())) {
      integrationConsumer.accept(request);
    }
  }
}
