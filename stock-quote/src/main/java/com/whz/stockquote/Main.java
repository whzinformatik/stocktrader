package com.whz.stockquote;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        String serviceName = System.getenv("RABBITMQ_SERVICE");
        String exchangeName = System.getenv("RABBITMQ_EXCHANGE");
        String exchangeType = System.getenv("RABBITMQ_EXCHANGE_TYPE");

        List<String> symbols = Arrays.asList("TSLA", "INTC", "GOOG", "MSFT", "AMZN", "NVDA", "NFLX", "SBUX", "SAP", "ADDYY", "POAHF", "SAP.DE");

        StockQuotePublisher stockQuotePublisher = new StockQuotePublisher(serviceName, exchangeName, exchangeType);

        stockQuotePublisher.run(symbols);
    }
}