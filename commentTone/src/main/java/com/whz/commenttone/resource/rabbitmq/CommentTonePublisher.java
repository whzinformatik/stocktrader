package com.whz.commenttone.resource.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.vlingo.common.serialization.JsonSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class CommentTonePublisher<T> {

    private final ConnectionFactory connectionFactory;

    private final Logger logger = LoggerFactory.getLogger(CommentTonePublisher.class);

    public CommentTonePublisher(String host) {
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setHost(host);
    }

    public void send(String exchangeName, T message) {

        try (final Connection connection = connectionFactory.newConnection();
             final Channel channel = connection.createChannel()) {

            logger.info("sending comment: " + message.toString());

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
