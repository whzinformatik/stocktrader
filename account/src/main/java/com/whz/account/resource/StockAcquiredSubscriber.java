package com.whz.account.resource;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import io.vlingo.actors.Logger;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public enum StockAcquiredSubscriber {
  INSTANCE;

  private final Logger logger = Logger.basicLogger();

  private static final String EXCHANGE_NAME = "logs"; // ???

  StockAcquiredSubscriber() {
    final ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");

    try (final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel()) {

      channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
      String queueName = channel.queueDeclare().getQueue();
      channel.queueBind(queueName, EXCHANGE_NAME, "");

      logger.debug("Waiting for messages..");

      DeliverCallback deliverCallback =
          (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            logger.debug("Received '" + message + "' with a length of: " + message.length());
          };
      String test = channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});

      logger.debug(test);
    } catch (IOException | TimeoutException e) {
      logger.debug(e.getMessage(), e);
    }
  }
}
