package com.whz.commenttone.resource.rabbitmq;

import com.rabbitmq.client.*;
import io.vlingo.actors.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class CommentToneConsumer {

    private final ConnectionFactory connectionFactory;

    private final Logger logger = Logger.basicLogger();

    public CommentToneConsumer(String host) {
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setHost(host);
    }

    public void consume(String exchangeName, String queueName) throws IOException, TimeoutException {

        logger.info("receiving feedback...");

        try (final Connection connection = connectionFactory.newConnection();
             final Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(exchangeName, queueName);

            channel.queueBind(queueName, exchangeName, "");

            final Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, StandardCharsets.UTF_8);
                    logger.info("received '" + message + "'");
                }
            };

            channel.basicConsume(queueName, true, consumer);
        }
    }
}
