package com.whz.commenttone.resource.rabbitmq;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class CommentToneConsumer {

    private final ConnectionFactory connectionFactory;

    private final Logger logger = LoggerFactory.getLogger(CommentToneConsumer.class);

    public CommentToneConsumer(String host) {
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setHost(host);
    }

    public void consume(String exchangeName) {

        logger.info("receiving feedback...");

        try (final Connection connection = connectionFactory.newConnection();
             final Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(exchangeName, "fanout");

            String queueName = channel.queueDeclare().getQueue();

            logger.info("queueName is " + queueName);

            channel.queueBind(queueName, exchangeName, "");

            final Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    String message = new String(body, StandardCharsets.UTF_8);
                    logger.info("received feedback: " + message);
                }
            };

            channel.basicConsume(queueName, true, consumer);
        } catch (IOException | TimeoutException ex) {
            logger.info("can't receive feedback: " + ex.getMessage());
        }
    }
}
