/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
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

public class Publisher<T> {

  private final ConnectionFactory connectionFactory;

  public Publisher(String host) {
    this.connectionFactory = new ConnectionFactory();
    this.connectionFactory.setHost(host);
  }

  public void send(String exchangeName, T message) throws IOException, TimeoutException {
    try (final Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel()) {
      channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);

      channel.basicPublish(
          exchangeName,
          "",
          null,
          JsonSerialization.serialized(message).getBytes(StandardCharsets.UTF_8));
    }
  }
}
