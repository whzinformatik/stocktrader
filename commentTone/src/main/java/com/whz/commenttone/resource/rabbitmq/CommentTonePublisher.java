package com.whz.commenttone.resource.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.vlingo.actors.Logger;
import io.vlingo.common.serialization.JsonSerialization;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class CommentTonePublisher<T> {

    private final ConnectionFactory connectionFactory;

    private final Logger logger = Logger.basicLogger();

    public CommentTonePublisher(String host) {
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setHost(host);
    }

    public void send(String exchangeName, String queueName, T message) throws IOException, TimeoutException {

        logger.info("sending comment...");

        try (final Connection connection = connectionFactory.newConnection();
             final Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);

            channel.basicPublish(
                    exchangeName,
                    "",
                    null,
                    JsonSerialization.serialized(message).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
