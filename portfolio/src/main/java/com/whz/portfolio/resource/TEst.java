package com.whz.portfolio.resource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class TEst {

	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

			String message = "info: Hello World!";

			while (true) {
				Thread.sleep(100000);
				channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
				System.out.println(" [x] Sent '" + message + "'");
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e1) {
			e1.printStackTrace();
		}
	}

}
