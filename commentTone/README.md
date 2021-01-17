# Comment tone

[![commentTone](https://github.com/whzinformatik/stocktrader/workflows/commentTone/badge.svg)][comment_tone_actions]

This part of project receives feedbacks for stocktrader application and send it to account project.

### Data structure
```
public class CommentTone {
    public String id;
    public String message;
    public Sentiment *sentiment;
}

public enum Sentiment {
    UNKNOWN,
    POSITIVE,
    NEUTRAL,
    NEGATIVE
}
```

*_In this case the sentiment would be generated from random numbers from 0 to 12 (0-3 is unknown, 4-6 is negative, 7-9 is neutral, 10-12 is positive)_   

---

### Usage

- declare as a service in `docker-compose.stocktrader.yml`
- set environment variables for RabbitMQ in `docker-compose.stocktrader.yml`:
  - `RABBITMQ_SERVICE` – name of the RabbitMQ service
  - `RABBITMQ_PUBLISH_EXCHANGE`– name of the exchange from which feedbacks consumed
  - `RABBITMQ_CONSUME_EXCHANGE`– name of the exchange which comments get sent to
  - `RABBITMQ_EXCHANGE_TYPE`– type of the exchange which comments get sent to
- run with `docker-compose -f docker-compose.yml -f docker-compose.stocktrader.yml up -d --build`:
  - build process will run `mvn clean package` first (if there are any changes)
  - feedbacks consumer and comments publisher container with the previously built jar will get started
- implement a feedback consumer:
  - declare a message queue
  - bind the queue to the specified exchange to consume messages
- implement a comments publisher:
    - declare a message queue
    - bind the queue to the specified exchange to consume messages

---

### Quick and dirty test

#### Test feedback consumer

The following class can be run as a publisher which will publish feedback:

(HINTS: `docker-compose -f docker-compose.yml -f docker-compose.stocktrader.yml up -d --build` should have executed without errors and `RABBITMQ_CONSUME_EXCHANGE` is set to "feedback" and `RABBITMQ_EXCHANGE_TYPE` is set to "fanout" in this example)

```
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

```
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

---

## Documentation

You can get more details about the project with the documented code or the domain stories in the [`doc`][documentation] folder.

[comment_tone_actions]: https://github.com/whzinformatik/stocktrader/actions?query=workflow%3AcommentTone
[documentation]: ./doc
