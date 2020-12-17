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

/**
 * This class can be used to receive a json message from rabbitmq.
 *
 * @since 1.0.0
 */
public class CommentToneSubscriber {

  /**
   * Variables, that uses environment variables so it is possible to change them more easily
   *
   * @since 1.0.0
   */
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

  /**
   * Create a subscriber which is connected to a specific rabbitmq instance.
   *
   * @since 1.0.0
   */
  public CommentToneSubscriber() {
    this.connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(serviceName);

    publisher = new CommentTonePublisher<>(serviceName);
  }

  /**
   * Consume a new message from the rabbitmq instance.
   *
   * @throws IOException if an error is encountered
   * @throws TimeoutException if a blocking operation times out
   * @since 1.0.0
   */
  public void consume() throws IOException, TimeoutException {
    try (final Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel()) {

      /*
       * Create exchange with given exchange type to consume comment-tone message
       */
      channel.exchangeDeclare(consumeExchangeName, exchangeType);

      /*
       * Create a non-durable, exclusive, auto-delete queue with a generated name
       */
      String queueName = channel.queueDeclare().getQueue();

      /*
       * Append messages from named exchange to named queue
       */
      channel.queueBind(queueName, consumeExchangeName, "");

      logger.info("Receiving feedback");

      DeliverCallback deliverCallback =
          ((consumerTag, delivery) -> {
            String feedbackMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);

            logger.info("Received feedback: " + feedbackMessage);

            CommentTone comment =
                new GsonBuilder().create().fromJson(feedbackMessage, CommentTone.class);

            /*
             * Add randomly generated sentiment to comment-tone
             */
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
