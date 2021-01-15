/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.resource;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.vlingo.actors.Logger;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;

public enum StockAcquiredPublisher {
  INSTANCE;

  private final Logger logger = Logger.basicLogger();

  private final String exchangeName;
  private final String exchangeType;
  private final ConnectionFactory connectionFactory;

  StockAcquiredPublisher() {
    String serviceName = System.getenv("RABBITMQ_SERVICE");
    exchangeName = System.getenv("RABBITMQ_EXCHANGE");
    exchangeType = System.getenv("RABBITMQ_EXCHANGE_TYPE");

    connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(serviceName);
    logger.debug("Started stock acquired publisher");
  }

  public void send(double data) {
    try (final Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel()) {
      channel.exchangeDeclare(exchangeName, exchangeType);
      channel.basicPublish(exchangeName, "", null, toBytes(data));
      logger.debug("Stock acquired publisher sending: " + data);
    } catch (IOException | TimeoutException e) {
      logger.debug(e.getMessage());
    }
  }

  private byte[] toBytes(double data) {
    byte[] result = new byte[8];
    ByteBuffer.wrap(result).putDouble(data);
    return result;
  }
}
