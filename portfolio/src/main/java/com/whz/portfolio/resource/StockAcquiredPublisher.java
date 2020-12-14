package com.whz.portfolio.resource;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.whz.portfolio.infrastructure.StockQuoteData;

import io.vlingo.actors.Logger;
import io.vlingo.common.serialization.JsonSerialization;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.concurrent.TimeoutException;

public enum StockAcquiredPublisher {
	INSTANCE("localhost");

	private final Logger logger = Logger.basicLogger();
	private final String EXCHANGE_NAME = "logs";

	private final ConnectionFactory connectionFactory;

	StockAcquiredPublisher(String host) {
		connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(host);
	}

	public <T> void send(T data) throws IOException, TimeoutException {
		try (final Connection connection = connectionFactory.newConnection();
				final Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
			byte[] body = JsonSerialization.serialized(data).getBytes(StandardCharsets.UTF_8);
			channel.basicPublish(EXCHANGE_NAME, "", null, body);
		}
	}
	
	public <T> void send(double amount) {
		try (final Connection connection = connectionFactory.newConnection();
				final Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
			byte[] body = new byte[8];
			ByteBuffer.wrap(body).putDouble(amount);
			channel.basicPublish(EXCHANGE_NAME, "", null, body);
		} catch (IOException e) {
			logger.debug(e.getMessage());
		} catch (TimeoutException e) {
			logger.debug(e.getMessage());
		}
	}

}
