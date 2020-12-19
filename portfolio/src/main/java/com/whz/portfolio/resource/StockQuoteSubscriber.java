package com.whz.portfolio.resource;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.whz.portfolio.infrastructure.StockQuoteData;
import io.vlingo.actors.Logger;
import io.vlingo.common.serialization.JsonSerialization;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public enum StockQuoteSubscriber {
	INSTANCE;

	private final Logger logger = Logger.basicLogger();
	private final Map<String, StockQuoteData> stockQuotes = new HashMap<>();
	private Connection connection;

	StockQuoteSubscriber() {
		String serviceName = System.getenv("RABBITMQ_SERVICE");
	    String exchangeName = System.getenv("RABBITMQ_EXCHANGE");
	    String exchangeType = System.getenv("RABBITMQ_EXCHANGE_TYPE");
	    if(serviceName == null || exchangeName == null ||exchangeType == null) {
	    	throw new RuntimeException("Please set env variables for RabbitMQ!");
	    }
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(serviceName);
			connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(exchangeName, exchangeType);
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, exchangeName, "");
			logger.debug("Started stock quote subscriber");
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
				StockQuoteData stockQuoteData = deserialized(message);
				stockQuotes.put(stockQuoteData.symbol, stockQuoteData);
				logger.debug(
						"Stock quote subscriber received '" + stockQuoteData.symbol + "' with a length of: " + message.length());
			};
			channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
			});
		} catch (IOException | TimeoutException e) {
			logger.debug(e.getMessage(), e);
		}
	}

	private StockQuoteData deserialized(String data) {
		return JsonSerialization.deserialized(data, StockQuoteData.class);
	}

	/**
	 * 
	 * @return All StockQuoteData objects from the cache
	 */
	public Collection<StockQuoteData> get() {
		return stockQuotes.values();
	}

	/**
	 * 
	 * @param symbol
	 * @param time
	 * @return The StockQuoteData object with the matching symbol
	 */
	public StockQuoteData get(String symbol) {
		return stockQuotes.get(symbol);
	}

	/**
	 * 
	 * @param symbols
	 * @return All StockQuoteData objects with matching symbols
	 */
	public ArrayList<StockQuoteData> get(Collection<String> symbols) {
		ArrayList<StockQuoteData> result = new ArrayList<>();
		symbols.forEach(symbol -> {
			StockQuoteData data = stockQuotes.get(symbol);
			if (data != null) {
				result.add(data);
			}
		});
		return result;
	}

}
