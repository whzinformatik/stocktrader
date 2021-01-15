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

import com.whz.feedback.exchange.FeedbackDTO;
import com.whz.feedback.exchange.Publisher;
import com.whz.feedback.utils.EnvUtils;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PublisherIT {

  private Publisher<FeedbackDTO> publisher;

  private TestSubscriber<FeedbackDTO> subscriber;

  @BeforeEach
  public void setup() throws IOException, TimeoutException {
    String host = EnvUtils.RABBITMQ_SERVICE.get();
    publisher = new Publisher<>(host);
    subscriber = new TestSubscriber<>(host);
  }

  @Test
  public void testSend() throws IOException, TimeoutException {
    String exchangeName = "test_exchange";
    FeedbackDTO feedback = new FeedbackDTO("0", "test");

    subscriber.receive(exchangeName, FeedbackDTO.class);
    publisher.send(exchangeName, feedback);
    await().until(subscriber::containsData);

    List<FeedbackDTO> messages = subscriber.getMessages();
    assertThat(messages.size()).isEqualTo(1);

    FeedbackDTO message = subscriber.lastMessage();
    assertThat(message.id).isEqualTo(feedback.id);
    assertThat(message.message).isEqualTo(feedback.message);
  }
}
