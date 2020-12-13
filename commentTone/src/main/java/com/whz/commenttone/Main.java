package com.whz.commenttone;

import com.google.gson.GsonBuilder;
import com.rabbitmq.client.*;
import com.whz.commenttone.model.CommentTone;
import com.whz.commenttone.model.Feedback;
import com.whz.commenttone.rabbitmq.CommentTonePublisher;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class Main {

  private static final String serviceName = "localhost";
  private static final String publishExchangeName = "commentTone";
  private static final String consumeExchangeName = "feedback";

  private static final ConnectionFactory connectionFactory = new ConnectionFactory();

  private static final Logger logger = Logger.getLogger(Main.class.getSimpleName());

  public static void main(String[] args) {

    connectionFactory.setHost(serviceName);

    try (final Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel()) {

      channel.exchangeDeclare(consumeExchangeName, BuiltinExchangeType.FANOUT);

      String queueName = channel.queueDeclare().getQueue();

      channel.queueBind(queueName, consumeExchangeName, "");

      logger.info("Receiving feedback");

      DeliverCallback deliverCallback =
          ((consumerTag, delivery) -> {
            String feedbackMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);

            logger.info("Received feedback: " + feedbackMessage);

            Feedback feedback =
                new GsonBuilder().create().fromJson(feedbackMessage, Feedback.class);

            CommentTone comment = new CommentTone(feedback.id, feedback.message);

            System.out.println("feedbacks : " + feedbackMessage);

            CommentTonePublisher publisher =
                new CommentTonePublisher(publishExchangeName, serviceName);
            publisher.publish(comment);
          });

      channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    } catch (IOException | TimeoutException exception) {
      exception.printStackTrace();
    }
  }
}
