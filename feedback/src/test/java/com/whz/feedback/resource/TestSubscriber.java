/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.resource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.whz.feedback.model.feedback.FeedbackState;
import com.whz.feedback.utils.EnvUtils;
import io.vlingo.common.serialization.JsonSerialization;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

class TestSubscriber<T> {

  private final Connection connection;

  private final List<T> messages;

  public TestSubscriber(String host) throws IOException, TimeoutException {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(host);
    this.connection = connectionFactory.newConnection();
    this.messages = new ArrayList<>();
  }

  public TestSubscriber(ConnectionFactory connectionFactory) throws IOException, TimeoutException {
    this.connection = connectionFactory.newConnection();
    this.messages = new ArrayList<>();
  }

  public void receive(String exchangeName, Class<T> clazz) throws IOException {
    final Channel channel = connection.createChannel();
    assertNotNull(channel);

    channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);
    String queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, exchangeName, "");

    DeliverCallback deliverCallback =
        (consumerTag, message) -> {
          String m = new String(message.getBody(), StandardCharsets.UTF_8);
          messages.add(JsonSerialization.deserialized(m, clazz));
        };

    channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
  }

  public boolean containsData() {
    return !messages.isEmpty();
  }

  public List<T> getMessages() {
    return messages;
  }

  public T lastMessage() {
    return messages.get(messages.size() - 1);
  }
}
