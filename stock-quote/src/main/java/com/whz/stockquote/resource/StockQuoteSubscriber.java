package com.whz.stockquote.resource;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class StockQuoteSubscriber {

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
                String symbol = "SAP";
                channel.basicPublish("", REQUEST_QUEUE, null, symbol.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + symbol + "'");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String stock = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [x] Received '" + stock + "'");
                };
                channel.basicConsume(RESPONSE_QUEUE, true, deliverCallback, consumerTag -> {
                });
            }
        }
    }
}
