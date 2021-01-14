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
import com.whz.portfolio.infrastructure.StockAcquiredData;
import io.vlingo.actors.Logger;
import io.vlingo.common.serialization.JsonSerialization;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ publisher. Notifies the account every time a purchase was made.
 *
 * @since 1.0.0
 */
public enum StockAcquiredPublisher {
  INSTANCE;

  private final Logger logger = Logger.basicLogger();

  private final String exchangeName;
  private final String exchangeType;
  private final ConnectionFactory connectionFactory;

  StockAcquiredPublisher() {
    String serviceName = getenv("RABBITMQ_SERVICE", "localhost");

    exchangeName = getenv("RABBITMQ_STOCK_ACQUIRED_EXCHANGE", "StockAcquiredPublisher");
    exchangeType = getenv("RABBITMQ_STOCK_ACQUIRED_EXCHANGE_TYPE", "fanout");

    connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(serviceName);
    logger.debug("Started stock acquired publisher");
  }

  /**
   * Notifies the account about a purchase.
   *
   * @param id of the account
   * @param value - the money spent
   */
  public void send(String id, double value) {
    try (final Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel()) {
      channel.exchangeDeclare(exchangeName, exchangeType);
      StockAcquiredData data = new StockAcquiredData(id, value);
      String message = JsonSerialization.serialized(data);
      channel.basicPublish(exchangeName, "", null, message.getBytes());
      logger.debug("Stock acquired publisher sending: " + data);
    } catch (IOException | TimeoutException e) {
      logger.debug(e.getMessage());
    }
  }

  /**
   * Helper method to receive an environment variable.
   *
   * @param key
   * @param alt
   * @return The environment variable. If not found the alt param is returned.
   */
  private String getenv(String key, String alt) {
    String result = System.getenv(key);
    return result == null ? alt : result;
  }
}
