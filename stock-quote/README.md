# Stock-Quote

### Usage

- declare as a service in `docker-compose.yml`
- set environment variables for RabbitMQ in `docker-compose.yml`: 
  - `RABBITMQ_SERVICE` – name of the RabbitMQ service
  - `RABBITMQ_EXCHANGE`– name of the exchange which stocks get sent to
  - `RABBITMQ_EXCHANGE_TYPE` – type of the exchange which stocks get sent to
  - `RANDOM_PUBLISH_INTERVAL`– set to "true" to let stocks (messages) get published in random intervals (< 5 seconds), otherwise they will get published every 30 seconds 
- run with `docker-compose up -d --build`:
  - build process will run `mvn clean package` first (if there are any changes)
  - publisher container with the previously built jar will get started 
- implement a subscriber:
  - declare a message queue
  - bind the queue to the specified exchange to consume messages

---
 
### Possible changes / improvements in the future

- make stock requests more adjustable (outside of the code):
    - environment variable in `docker-compose.yml`
    - better: handle requests for certain stocks (coming from portfolio project) through messaging
- make publish interval more adjustable (certain interval, not random etc.)

---

### Quick and dirty test

The following class can be run as a subscriber which will receive published stock messages and print them to the console:

(HINTS: `docker-compose up -d --build` should have executed without errors and `RABBITMQ_EXCHANGE` is set to "stocks" in this example)

```
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class StockQuoteSubscriber {

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare("stock-queue", false, false, false, null);
            channel.queueBind("stock-queue", "stocks", "");
            while (true) {
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String stock = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [x] Received '" + stock + "'");
                };
                channel.basicConsume("stock-queue", true, deliverCallback, consumerTag -> {
                });
            }
        }
    }
}
```
Currently used stocks (companies) are:
- Tesla
- Intel
- Google
- Microsoft
- Amazon
- NVidia
- Netflix
- Starbucks
- Adidas
- Porsche
- SAP (NASDAQ)
- SAP (Xetra)