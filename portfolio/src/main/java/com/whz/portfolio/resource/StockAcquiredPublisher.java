/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.resource;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.whz.portfolio.infrastructure.StockAcquiredData;
import io.vlingo.actors.Logger;
import io.vlingo.common.serialization.JsonSerialization;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ publisher. Notifies the account every time a purchase was made.
 *
 * @since 1.0.0
 */
public enum StockAcquiredPublisher {
  INSTANCE;

  private final Logger logger = Logger.basicLogger();

  private final String exchangeName;
  private final String exchangeType;
  
  private Connection connection;
  private Channel channel;

  StockAcquiredPublisher() {
    String serviceName = getenv("RABBITMQ_SERVICE", "localhost");

    exchangeName = getenv("RABBITMQ_STOCK_ACQUIRED_EXCHANGE", "stocks-acquired");
    exchangeType = getenv("RABBITMQ_STOCK_ACQUIRED_EXCHANGE_TYPE", "fanout");

    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(serviceName);
    
    try {
        connection = connectionFactory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, exchangeType);
    } catch (IOException | TimeoutException e) {
    	logger.debug("Failed to init the StockAcquiredPublisher", e);
	}
    		
    logger.debug("Started stock acquired publisher");
  }

  /**
   * Notifies the account about a purchase.
   *
   * @param id of the account
   * @param value - the money spent
   */
  public void send(String id, double value) {
      StockAcquiredData data = new StockAcquiredData(id, value);
      String message = JsonSerialization.serialized(data);
      try {
    	logger.debug("Stock acquired publisher sending: {}", message);
		channel.basicPublish(exchangeName, "", null, message.getBytes());
	  } catch (IOException e) {
		logger.debug("Failed to send message with id: " + id, e);
	  }
      
  }
  
  /**
   * Closes the connection and channel.
   */
  public void stop() {
	  try {
		connection.close();
		channel.close();
	  } catch (IOException | TimeoutException e) {
		logger.debug("Failed to stop the StockAcquiredPublisher", e);
	}  
  }

  /**
   * Helper method to receive an environment variable.
   *
   * @param key
   * @param alt - alternative string
   * @return The environment variable. If not found the alt param is returned.
   */
  private String getenv(String key, String alt) {
    String result = System.getenv(key);
    return result == null ? alt : result;
  }
}
