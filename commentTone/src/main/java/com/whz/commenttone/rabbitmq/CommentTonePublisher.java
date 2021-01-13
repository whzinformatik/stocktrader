/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.kb1prb13@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.whz.commenttone.rabbitmq;

import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * This class can be used to publish any object as json message to rabbitmq.
 *
 * @param <T> object type for the serialization
 * @since 1.0.0
 */
public class CommentTonePublisher<T> {

  private final ConnectionFactory connectionFactory;

  private final Logger logger = LoggerFactory.getLogger(CommentTonePublisher.class.getName());

  /**
   * Create a publisher which is connected to a specific rabbitmq instance.
   *
   * @param serviceName address of the rabbitmq instance
   * @since 1.0.0
   */
  public CommentTonePublisher(String serviceName) {
    this.connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(serviceName);
  }

  /**
   * Publish a new message to the rabbitmq instance.
   *
   * @param exchangeName name of the exchange
   * @param exchangeType type of exchange
   * @param message object for the serialization
   * @since 1.0.0
   */
  public void publish(String exchangeName, String exchangeType, T message) {
    try (final Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel()) {
      logger.info(
          "Publishing comment "
              + new GsonBuilder().create().toJson(message)
              + " in "
              + exchangeName
              + " exchange");

      channel.exchangeDeclare(exchangeName, exchangeType);

      channel.basicPublish(
          exchangeName,
          "",
          null,
          new GsonBuilder().create().toJson(message).getBytes(StandardCharsets.UTF_8));
    } catch (IOException | TimeoutException exception) {
      logger.debug(exception.getMessage(), exception);
    }
  }
}
