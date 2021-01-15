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
import com.rabbitmq.client.DeliverCallback;
import com.whz.commenttone.model.CommentTone;
import com.whz.commenttone.model.Sentiment;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  private final Logger logger =
      LoggerFactory.getLogger(CommentToneSubscriber.class.getSimpleName());

  /**
   * Create a subscriber which is connected to a specific rabbitmq instance.
   *
   * @since 1.0.0
   */
  public CommentToneSubscriber() {
    this.connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(serviceName);

    publisher = new CommentTonePublisher<>(serviceName, publishExchangeName, exchangeType);
  }

  /**
   * Consume a new message from the rabbitmq instance.
   *
   * @since 1.0.0
   */
  public void consume() {
    try (final Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel()) {

      // Create exchange with given exchange type to consume comment-tone message
      channel.exchangeDeclare(consumeExchangeName, exchangeType);

      // Create a non-durable, exclusive, auto-delete queue with a generated name
      String queueName = channel.queueDeclare().getQueue();

      // Append messages from named exchange to named queue
      channel.queueBind(queueName, consumeExchangeName, "");

      while (true) {
        DeliverCallback deliverCallback =
            ((consumerTag, delivery) -> {
              String feedbackMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);

              logger.info("Received feedback: " + feedbackMessage);

              CommentTone comment =
                  new GsonBuilder().create().fromJson(feedbackMessage, CommentTone.class);

              // Add randomly generated sentiment to comment
              int randomNumber = new Random().nextInt(13);

              comment.setSentiment(
                  randomNumber < 4
                      ? Sentiment.UNKNOWN
                      : randomNumber < 7
                          ? Sentiment.NEGATIVE
                          : randomNumber < 10 ? Sentiment.NEUTRAL : Sentiment.POSITIVE);

                publisher.publish(comment);
            });

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
      }
    } catch (TimeoutException | IOException exception) {
      logger.debug(exception.getMessage(), exception);
    }
  }
}
