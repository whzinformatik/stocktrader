package com.whz.portfolio.resource;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class StockQuotePublisherDemo {

  private static final String EXCHANGE_NAME = "logs";

  public static void main(String[] args) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()) {
      channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
      // send data
      for (int i = 0; i < 100; i++) {
        Thread.sleep((long) (Math.random() * 1000));
        String message = createSample("SomeStock" + i);
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
      }

    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TimeoutException e1) {
      e1.printStackTrace();
    }
  }

  private static String createSample(String name) {
    return "{'symbol':'" + name.toUpperCase() + "','displayName':'" + name + "'}";
  }
}
