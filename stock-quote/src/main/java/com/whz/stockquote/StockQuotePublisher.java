/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.stockquote;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * This class can be used to publish stock quote data to RabbitMQ. The stock quotes are obtained
 * from the "Finance Quotes API for Yahoo Finance" by Stijn Strickx.
 *
 * @see <a href="https://github.com/sstrickx/yahoofinance-api">yahoofinance-api by Stijn Strickx</a>
 * @since 1.0.0
 */
class StockQuotePublisher {

  private static final Logger logger = LoggerFactory.getLogger(StockQuotePublisher.class);

  private final String exchangeName;
  private final boolean durableExchange;

  private final ConnectionFactory connectionFactory;
  private Connection connection;

  /**
   * Create a publisher which is connected to a specific RabbitMQ instance.
   *
   * @param serviceName name of RabbitMQ host
   * @param exchangeName name of RabbitMQ exchange
   * @param durableExchange true if RabbitMQ exchange should be durable, false otherwise
   * @since 1.0.0
   */
  public StockQuotePublisher(String serviceName, String exchangeName, boolean durableExchange) {
    this.exchangeName = exchangeName;
    this.durableExchange = durableExchange;

    this.connectionFactory = new ConnectionFactory();
    this.connectionFactory.setHost(serviceName);
  }

  /**
   * Retrieve stock information and publish a new message to the RabbitMQ instance. The message
   * contains a string in JSON format with following information of a stock:
   *
   * <ul>
   *   <li>symbol
   *   <li>name
   *   <li>price
   *   <li>currency
   *   <li>time
   * </ul>
   *
   * @param symbols list of official stock symbols
   * @param channel RabbitMQ channel which messages will get published into
   * @see <a href="https://www.nasdaq.com/market-activity/stocks/screener">Nasdaq website with list
   *     of official stocks</a>
   * @since 1.0.0
   */
  private void publish(List<String> symbols, Channel channel) {
    try {
      channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, durableExchange);
      for (String s : symbols) {
        Stock answer = YahooFinance.get(s);
        try {
          if (answer == null)
            throw new StockNotFoundException(
                String.format("Could not find stock for symbol '%s'! Symbol will be ignored!", s));
        } catch (StockNotFoundException stockNotFoundException) {
          logger.error(stockNotFoundException.toString());
          continue;
        }
        String answerJsonString =
            new JSONObject()
                .put("symbol", answer.getSymbol())
                .put("longName", answer.getName())
                .put("regularMarketPrice", answer.getQuote().getPrice())
                .put("financialCurrency", answer.getCurrency())
                .put("regularMarketTime", answer.getQuote().getLastTradeTime().getTime().getTime())
                .toString();
        channel.basicPublish(
            exchangeName, "", null, answerJsonString.getBytes(StandardCharsets.UTF_8));
      }
    } catch (IOException e) {
      logger.error(e.toString());
    }
  }

  /**
   * Creates a connection and a channel to publish messages to RabbitMQ. Publish messages to
   * RabbitMQ continuously. The publishing interval may be a random number within zero and the
   * {@code publishInterval} as an upper limit.
   *
   * @param symbols list of official stock symbols
   * @param publishInterval time interval in seconds
   * @param publishRandomly true if publishing interval should be random, false otherwise
   * @since 1.0.0
   */
  public void run(List<String> symbols, int publishInterval, boolean publishRandomly) {
    try {
      connection = connectionFactory.newConnection();
      Channel channel = connection.createChannel();

      int actualPublishInterval = publishInterval * 1000;

      while (true) {
        publish(symbols, channel);

        if (publishRandomly) {
          actualPublishInterval = (int) (Math.random() * publishInterval * 1000);
        }
        logger.info(String.format("Publishing interval: %dms", actualPublishInterval));

        try {
          Thread.sleep(actualPublishInterval);
        } catch (InterruptedException e) {
          logger.error(e.toString());
        }
      }
    } catch (IOException | TimeoutException e) {
      logger.error(e.toString());
    }
  }

  /** Exception which is thrown if a stock could not get retrieved. */
  private static class StockNotFoundException extends Exception {
    public StockNotFoundException(String message) {
      super(message);
    }
  }
}
