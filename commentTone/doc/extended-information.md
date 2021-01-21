# Comment tone

### Quick and dirty test

#### Test feedback consumer

The following class can be run as a publisher which will publish feedback:

(HINTS: `docker-compose -f docker-compose.yml -f docker-compose.stocktrader.yml up -d --build` should have executed without errors and `RABBITMQ_CONSUME_EXCHANGE` is set to "feedback" and `RABBITMQ_EXCHANGE_TYPE` is set to "fanout" in this example)

```Java
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class FeedbackPublisher {

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {
      channel.exchangeDeclare("feedback", "fanout");

      FeedbackDTO message = new FeedbackDTO("135", "some message");

      channel.basicPublish(
              "feedback",
              "",
              null,
              new GsonBuilder().create().toJson(message).getBytes(StandardCharsets.UTF_8));
    }
  }

  public static class FeedbackDTO {
    public final String id;
    public final String message;

    public FeedbackDTO(String id, String message) {
      this.id = id;
      this.message = message;
    }
  }
}
```

#### Test comment publisher

The following class can be run as a subscriber which will receive published comment messages and print them to the console:

(HINTS: `docker-compose -f docker-compose.yml -f docker-compose.stocktrader.yml up -d --build` should have executed without errors and `RABBITMQ_PUBLISH_EXCHANGE` is set to "commentTone" and `RABBITMQ_EXCHANGE_TYPE` is set to "fanout" in this example)

```Java
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class CommentSubscriber {

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");

    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {

      channel.exchangeDeclare("commentTone", "fanout");

      String queueName = channel.queueDeclare().getQueue();
      channel.queueBind("comment-queue", "commentTone", "");

      while (true) {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String comment = new String(delivery.getBody(), StandardCharsets.UTF_8);
          System.out.println(" Received '" + comment + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
      }
    }
  }
}
```
