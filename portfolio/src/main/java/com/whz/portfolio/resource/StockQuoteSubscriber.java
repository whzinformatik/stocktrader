/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
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
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * RabbitMQ subscriber for receiving stock quotes.
 *
 * @since 1.0.0
 */
public enum StockQuoteSubscriber {
  INSTANCE;

  private final Logger logger = Logger.basicLogger();
  private final Map<String, StockQuoteData> stockQuotes;

  private Connection connection;
  private Channel channel;

  StockQuoteSubscriber() {
    stockQuotes = new HashMap<>();

    String serviceName = getenv("RABBITMQ_SERVICE", "localhost");
    String exchangeName = getenv("RABBITMQ_STOCK_QUOTE_EXCHANGE", "stocks");
    String exchangeType = getenv("RABBITMQ_STOCK_QUOTE_EXCHANGE_TYPE", "fanout");

    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost(serviceName);
      connection = factory.newConnection();
      channel = connection.createChannel();
      channel.exchangeDeclare(exchangeName, exchangeType);
      String queueName = channel.queueDeclare().getQueue();
      channel.queueBind(queueName, exchangeName, "");

      logger.debug("Started stock quote subscriber");
      DeliverCallback deliverCallback =
          (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            StockQuoteData stockQuoteData = deserialized(message);
            stockQuotes.put(stockQuoteData.symbol, stockQuoteData);

            logger.debug(
                "Stock quote subscriber received '{}' with a length of: {}",
                stockQuoteData.symbol,
                message.length());
          };

      channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    } catch (IOException | TimeoutException e) {
    	logger.debug("Failed to init the StockQuoteSubscriber", e);
    }
  }

  /** Closes the connection and channel. */
  public void stop() {
	  try {
		connection.close();
		channel.close();
	  } catch (IOException | TimeoutException e) {
		  logger.debug("Failed to stop the StockAcquiredPublisher", e);
	}  
  }

  private StockQuoteData deserialized(String data) {
    return JsonSerialization.deserialized(data, StockQuoteData.class);
  }

  /** @return All StockQuoteData objects from the cache */
  public Collection<StockQuoteData> get() {
    return stockQuotes.values();
  }

  /** @return The StockQuoteData object with the matching symbol */
  public StockQuoteData get(String symbol) {
    return stockQuotes.get(symbol);
  }

  /** @return All StockQuoteData objects with matching symbols */
  public List<StockQuoteData> get(Collection<String> symbols) {
    return symbols.stream()
        .map(stockQuotes::get)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  /**
   * Helper method to receive an environment variable.
   *
   * @param key
   * @param alt - alternative string
   * @return The environment variable. If not found the alt param is returned.
   */
  private String getenv(String key, String alt) {
    String result = System.getenv(key);
    return result == null ? alt : result;
  }
}
