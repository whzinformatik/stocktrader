/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.resource;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.vlingo.common.serialization.JsonSerialization;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * This class can be used to publish any object as json message to rabbitmq.
 *
 * @param <T> object type for the serialization
 * @since 1.0.0
 */
public class Publisher<T> {

  private final Connection connection;

  /**
   * Create a publisher which is connected to a specific rabbitmq instance.
   *
   * @param host address of the rabbitmq instance
   * @since 1.0.0
   */
  public Publisher(String host) throws IOException, TimeoutException {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(host);
    this.connection = connectionFactory.newConnection();
  }

  /**
   * Publish a new message to the rabbitmq instance.
   *
   * @param exchangeName name of the exchange
   * @param message object for the serialization
   * @throws IOException if an error is encountered
   * @throws TimeoutException if a blocking operation times out
   * @since 1.0.0
   */
  public void send(String exchangeName, T message) throws IOException, TimeoutException {
    try (final Channel channel = connection.createChannel()) {
      channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);

      channel.basicPublish(
          exchangeName,
          "",
          null,
          JsonSerialization.serialized(message).getBytes(StandardCharsets.UTF_8));
    }
  }
}
