/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.kb1prb13@gmail.com>
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
import com.whz.commenttone.model.CommentTone;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommentTonePublisher {

  private final String exchangeName;

  private final ConnectionFactory connectionFactory;

  private final Logger logger = Logger.getLogger(CommentTonePublisher.class.getName());

  public CommentTonePublisher(String exchangeName, String serviceName) {
    this.exchangeName = exchangeName;

    this.connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(serviceName);
  }

  public void publish(CommentTone message, String exchangeType) {
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
    } catch (IOException | TimeoutException e) {
      logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
    }
  }
}
