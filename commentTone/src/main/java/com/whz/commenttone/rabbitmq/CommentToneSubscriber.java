package com.whz.commenttone.rabbitmq;

import com.google.gson.GsonBuilder;
import com.rabbitmq.client.*;
import com.whz.commenttone.model.CommentTone;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Optional;

import java.util.Random;

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

  private final Logger logger = Logger.getLogger(CommentToneSubscriber.class.getSimpleName());

  public CommentToneSubscriber() {
    this.connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(serviceName);
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
            String sentiment = randomNumber < 4 ? "negative" : randomNumber < 8 ? "neutral" : "positive";

            comment.setSentiment(sentiment);

            CommentTonePublisher publisher =
                new CommentTonePublisher(publishExchangeName, serviceName);
            publisher.publish(comment, exchangeType);
          });

      channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    } catch (IOException | TimeoutException exception) {
      logger.log(Level.SEVERE, Arrays.toString(exception.getStackTrace()));
    }
  }
}