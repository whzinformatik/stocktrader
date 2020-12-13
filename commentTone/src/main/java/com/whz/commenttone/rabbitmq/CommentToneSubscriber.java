/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.kb1prb13@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.whz.commenttone.rabbitmq;

import com.google.gson.GsonBuilder;
import com.rabbitmq.client.*;
import com.whz.commenttone.model.CommentTone;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class CommentToneSubscriber {

  private final String serviceName =
      Optional.ofNullable(System.getenv("RABBITMQ_SERVICE")).orElse("localhost");
  private final String publishExchangeName =
      Optional.ofNullable(System.getenv("RABBITMQ_PUBLISH_EXCHANGE")).orElse("commentTone");
  private final String consumeExchangeName =
      Optional.ofNullable(System.getenv("RABBITMQ_CONSUME_EXCHANGE")).orElse("feedback");
  private final String exchangeType =
      Optional.ofNullable(System.getenv("RABBITMQ_EXCHANGE_TYPE")).orElse("fanout");

  private final ConnectionFactory connectionFactory;

  private final CommentTonePublisher<CommentTone> publisher;

  private final Logger logger = Logger.getLogger(CommentToneSubscriber.class.getSimpleName());

  public CommentToneSubscriber() {
    this.connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(serviceName);

    publisher = new CommentTonePublisher<>(serviceName);
  }

  public void consume() {
    try (final Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel()) {

      channel.exchangeDeclare(consumeExchangeName, exchangeType);

      String queueName = channel.queueDeclare().getQueue();

      channel.queueBind(queueName, consumeExchangeName, "");

      logger.info("Receiving feedback");

      DeliverCallback deliverCallback =
          ((consumerTag, delivery) -> {
            String feedbackMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);

            logger.info("Received feedback: " + feedbackMessage);

            CommentTone comment =
                new GsonBuilder().create().fromJson(feedbackMessage, CommentTone.class);

            int randomNumber = new Random().nextInt(11);
            String sentiment =
                randomNumber < 4 ? "negative" : randomNumber < 8 ? "neutral" : "positive";

            comment.setSentiment(sentiment);

              try {
                  publisher.publish(publishExchangeName, exchangeType, comment);
              } catch (TimeoutException e) {
                  e.printStackTrace();
              }
          });

      channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
  }
}
