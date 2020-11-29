package com.whz.portfolio.resource;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.whz.portfolio.infrastructure.StockQuoteData;

import io.vlingo.actors.Logger;
import io.vlingo.common.serialization.JsonSerialization;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.concurrent.TimeoutException;

public class StockQuotePublisherDemo {

	private static final Logger logger = Logger.basicLogger();
	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
			// send data
			for (int i = 0; i < 10; i++) {
				Thread.sleep((long) (Math.random() * 100));
				String message = createSample("SomeStock" + i);
				channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
			}

		} catch (IOException | TimeoutException e) {
			logger.debug(e.getMessage(), e);
		}
	}

	private static String createSample(String name) {
		StockQuoteData tmp = new StockQuoteData(name, name, "USD", Math.random() * 1000,
				System.currentTimeMillis());
		return JsonSerialization.serialized(tmp);
	}
}
