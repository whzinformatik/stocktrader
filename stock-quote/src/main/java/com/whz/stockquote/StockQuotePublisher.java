package com.whz.stockquote;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class StockQuotePublisher {

    private final String serviceName;
    private final String exchangeName;
    private final String exchangeType;


    public StockQuotePublisher(String serviceName, String exchangeName, String exchangeType) {
        this.serviceName = serviceName;
        this.exchangeName = exchangeName;
        this.exchangeType = exchangeType;
    }

    public void publish(List<String> symbols) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(serviceName);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchangeName, exchangeType);
            for (String s : symbols) {
                Stock answer = YahooFinance.get(s);
                channel.basicPublish(exchangeName, "", null, answer.toString().getBytes("UTF-8"));
            }

        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void run(List<String> symbols) throws IOException, InterruptedException {
        while (true) {
            Thread.sleep((long) (Math.random() * 3000));
            publish(symbols);
        }
    }
}
