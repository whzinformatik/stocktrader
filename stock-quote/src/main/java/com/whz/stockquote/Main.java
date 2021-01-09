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


/**
 * This class represents the main class for the stock quote application. It can be used to start
 * the program.
 *
 * @since 1.0.0
 */
public class Main {

    private static String serviceName;
    private static String exchangeName;
    private static int publishInterval;
    private static boolean publishRandomly = false;
    private static List<String> symbols;

    public static void main(String[] args) {
        manageRabbitSetup();

        managePublishInterval();

        manageStocks();

        StockQuotePublisher stockQuotePublisher =
                new StockQuotePublisher(serviceName, exchangeName);

        stockQuotePublisher.run(symbols, publishInterval, publishRandomly);
    }

    /**
     * Sets host and exchange name of RabbitMQ.
     *
     * @since 1.0.0
     */
    private static void manageRabbitSetup(){
        serviceName = Optional.ofNullable(System.getenv("RABBITMQ_SERVICE")).orElse("localhost");
        exchangeName = Optional.ofNullable(System.getenv("RABBITMQ_EXCHANGE")).orElse("stocks");
    }

    /**
     * Sets publishing interval (fixed or random (within boundaries)).
     *
     * @since 1.0.0
     */
    private static void managePublishInterval(){
        String publishIntervalString = Optional.ofNullable(System.getenv("PUBLISH_INTERVAL")).orElse("30000");
        publishInterval = Integer.parseInt(publishIntervalString);

        String randomPublishIntervalString = Optional.ofNullable(System.getenv("RANDOM_PUBLISH_INTERVAL")).orElse("0");
        if(!randomPublishIntervalString.equals("0")){
            publishInterval = Integer.parseInt(randomPublishIntervalString);
            publishRandomly = true;
        }
    }

    /**
     * Sets list of stock symbols whose information should be published to RabbitMQ.
     *
     * @since 1.0.0
     */
    private static void manageStocks() {
        String symbolsString = Optional.ofNullable(System.getenv("STOCK_SYMBOLS")).orElse("TSLA,INTC,GOOG,MSFT,AMZN,NVDA,NFLX,SBUX,SAP,ADDYY,POAHF,SAP.DE");
        symbols = Arrays.asList(symbolsString.split("\\s*,\\s*"));
    }
}
