package com.whz.stockquote.resource;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;

public class StockQuotePublisher {

    private static final String REQUEST_QUEUE = "stock_requests";
    private static final String RESPONSE_QUEUE = "stock_responses";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // TODO change to service name when dockerized
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(REQUEST_QUEUE, false, false, false, null);
            channel.queueDeclare(RESPONSE_QUEUE, false, false, false, null);
            while (true) {
                Thread.sleep(5000);
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String symbol = new String(delivery.getBody(), "UTF-8");
                    String answer = getQuotes(symbol);
                    channel.basicPublish("", RESPONSE_QUEUE, null, answer.getBytes("UTF-8"));
                    System.out.println(" [x] Sent '" + answer + "'");
                };
                channel.basicConsume(REQUEST_QUEUE, true, deliverCallback, consumerTag -> {
                });
            }
        }
    }

    public static String getQuotes(String... symbols) throws IOException {
        StringBuilder answer = new StringBuilder();
        for (String s : symbols) {
            Stock stock = YahooFinance.get(s);
            if (answer.toString().equals("")) {
                answer.append(stock.toString());
            } else {
                answer.append("\t").append(stock.toString());
            }
        }
        return answer.toString();
    }
}
