/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.stockquote;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.BasicConfigurator;

/**
 * This class represents the main class for the stock quote application. It can be used to start the
 * program.
 *
 * @since 1.0.0
 */
public class Main {

  /**
   * Name of the RabbitMQ service.
   *
   * @since 1.0.0
   */
  private final String serviceName;

  /**
   * Name of the RabbitMQ exchange.
   *
   * @since 1.0.0
   */
  private final String exchangeName;

  /**
   * Durability of the RabbitMQ exchange.
   *
   * @since 1.0.0
   */
  private final boolean durableExchange;

  /**
   * Time interval between each message publication (in seconds).
   *
   * @since 1.0.0
   */
  private int publishInterval;

  /**
   * Randomness of the {@code publishInterval}.
   *
   * @since 1.0.0
   */
  private boolean publishRandomly;

  /**
   * List of stock symbols.
   *
   * @since 1.0.0
   */
  private final List<String> symbols;

  /**
   * Create an object which contains information for the publisher setup.
   *
   * @since 1.0.0
   */
  public Main() {
    this.serviceName = Optional.ofNullable(System.getenv("RABBITMQ_SERVICE")).orElse("localhost");
    this.exchangeName = Optional.ofNullable(System.getenv("RABBITMQ_EXCHANGE")).orElse("stocks");

    String durableExchangeString =
        Optional.ofNullable(System.getenv("DURABLE_EXCHANGE")).orElse("false");
    this.durableExchange = Boolean.parseBoolean(durableExchangeString);

    String publishIntervalString =
        Optional.ofNullable(System.getenv("PUBLISH_INTERVAL")).orElse("3");
    this.publishInterval = Integer.parseInt(publishIntervalString);

    String randomPublishIntervalString =
        Optional.ofNullable(System.getenv("RANDOM_PUBLISH_INTERVAL")).orElse("0");
    if (!randomPublishIntervalString.equals("0")) {
      this.publishInterval = Integer.parseInt(randomPublishIntervalString);
      this.publishRandomly = true;
    }

    String symbolsString =
        Optional.ofNullable(System.getenv("STOCK_SYMBOLS"))
            .orElse("TSLA,INTC,GOOG,MSFT,AMZN,NVDA,NFLX,SBUX,SAP,ADDYY,POAHF,SAP.DE");
    this.symbols = Arrays.asList(symbolsString.toUpperCase().split("\\s*,\\s*"));
  }

  public static void main(String[] args) {
    BasicConfigurator.configure();

    Main m = new Main();
    StockQuotePublisher stockQuotePublisher =
        new StockQuotePublisher(m.serviceName, m.exchangeName, m.durableExchange);

    stockQuotePublisher.run(m.symbols, m.publishInterval, m.publishRandomly);
  }
}
