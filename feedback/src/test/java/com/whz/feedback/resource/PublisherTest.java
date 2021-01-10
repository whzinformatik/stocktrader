/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import com.whz.feedback.infrastructure.FeedbackData;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PublisherTest {

  private Publisher<FeedbackData> publisher;

  private TestSubscriber<FeedbackData> subscriber;

  @BeforeEach
  public void setup() throws IOException, TimeoutException {
    MockConnectionFactory mockConnectionFactory = new MockConnectionFactory();
    publisher = new Publisher<>(mockConnectionFactory);
    subscriber = new TestSubscriber<>(mockConnectionFactory);
  }

  @Test
  public void testSend() throws IOException, TimeoutException {
    String exchangeName = "test_exchange";
    FeedbackData feedback = FeedbackData.from("0", "test");

    subscriber.receive(exchangeName, FeedbackData.class);
    publisher.send(exchangeName, feedback);
    await().until(subscriber::containsData);

    List<FeedbackData> messages = subscriber.getMessages();
    assertThat(messages.size()).isEqualTo(1);

    FeedbackData message = subscriber.lastMessage();
    assertThat(message.id).isEqualTo(feedback.id);
    assertThat(message.message).isEqualTo(feedback.message);
  }
}
