/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.stockquote;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import org.json.JSONObject;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class StockQuotePublisher {

  private static final Logger LOGGER = Logger.getLogger(StockQuotePublisher.class.getName());

  private final String exchangeName;
  private final String exchangeType;

  private final ConnectionFactory connectionFactory;

  public StockQuotePublisher(String serviceName, String exchangeName, String exchangeType) {
    this.exchangeName = exchangeName;
    this.exchangeType = exchangeType;

    this.connectionFactory = new ConnectionFactory();
    this.connectionFactory.setHost(serviceName);
  }

  public void publish(List<String> symbols) throws IOException {
    try (Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel()) {
      channel.exchangeDeclare(exchangeName, exchangeType);
      for (String s : symbols) {
        Stock answer = YahooFinance.get(s);
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
    } catch (TimeoutException e) {
      LOGGER.severe(e.toString());
    }
  }

  public void run(List<String> symbols) throws IOException, InterruptedException {
    String randomPublishIntervalString = System.getenv("SET_RANDOM_PUBLISH_INTERVAL");

    if (Boolean.parseBoolean(randomPublishIntervalString)) {
      while (true) {
        publish(symbols);
        Thread.sleep((long) (Math.random() * 3000));
      }
    } else {
      LOGGER.warning(
          "Environment variable 'SET_RANDOM_PUBLISH_INTERVAL' either incorrect or not set!");
      while (true) {
        publish(symbols);
        Thread.sleep(30000);
      }
    }
  }
}
