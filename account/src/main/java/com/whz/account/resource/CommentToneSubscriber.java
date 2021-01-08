/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.resource;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.whz.account.infrastructure.CommentToneData;
import com.whz.account.model.account.Account;
import com.whz.account.model.account.AccountEntity;
import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.common.serialization.JsonSerialization;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * The CommentToneSubscriber which subscribes to a CommentTonePublisher endpoint and receives
 * messages containing a comment tone. After receiving a message the "sentimentReceived(Sentiment
 * sentiment)" method will be triggered which matches the id of the comment tone to our account and
 * changes the given sentiment to the new value by creating a SentimentReceived event.
 *
 * @author Lation
 */
public enum CommentToneSubscriber {
  INSTANCE;

  private final Logger logger = Logger.basicLogger();

  private Stage stage;

  /**
   * Initializes and establishes a RabbitMQ connection, waits for messages, receives them and calls
   * the appropriate sentimentReceived() method in the Account Interface.
   */
  CommentToneSubscriber() {
    String serviceName = System.getenv("RABBITMQ_SERVICE");
    String exchangeName = System.getenv("RABBITMQ_EXCHANGE_COMMENTTONE");
    String exchangeType = System.getenv("RABBITMQ_EXCHANGE_TYPE");

    try {
      final ConnectionFactory factory = new ConnectionFactory();
      factory.setHost(serviceName);
      final Connection connection = factory.newConnection();

      final Channel channel = connection.createChannel();
      channel.exchangeDeclare(exchangeName, exchangeType);
      String queueName = channel.queueDeclare().getQueue();
      channel.queueBind(queueName, exchangeName, "");

      logger.debug("Started comment tone subscriber");
      logger.debug("Waiting for messages...");

      DeliverCallback deliverCallback =
          (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            CommentToneData commentToneData =
                JsonSerialization.deserialized(message, CommentToneData.class);

            stage
                .actorOf(
                    Account.class,
                    stage.addressFactory().from(commentToneData.id),
                    AccountEntity.class)
                .andThenTo(
                    account -> {
                      return account.sentimentReceived(commentToneData.sentiment);
                    });

            logger.debug("Comment tone subscriber received message:'{}'", message);
          };

      channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    } catch (IOException | TimeoutException e) {
      logger.debug(e.getMessage(), e);
    }
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }
}
