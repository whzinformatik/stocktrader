# Stock-Quote

### Usage

- declare project as a service in `docker-compose.stocktrader.yml`
- set environment variables for RabbitMQ in `docker-compose.stocktrader.yml`:
    - `RABBITMQ_SERVICE` – name of the RabbitMQ service
    - `RABBITMQ_EXCHANGE`– name of the exchange which stocks get sent to
    - `DURABLE_EXCHANGE` – if set to "true", the RabbitMQ exchange will be durable
    - `PUBLISH_INTERVAL` – publishing interval of stock information in seconds
    - `RANDOM_PUBLISH_INTERVAL`– upper bound of publishing interval of stock information in seconds (setting this variable will override `PUBLISH_INTERVAL`)
    - `STOCKS` – comma separated string of official stock symbols
- run with `docker-compose -f docker-compose.yml -f docker-compose.stocktrader.yml up -d --build`:
    - Postgres and RabbitMQ will get started 
    - build process will run `mvn clean package` first (if there are any changes)
    - publisher container with the previously built jar will get started
- implement a subscriber (see code below):
    - declare a message queue
    - bind the queue to the specified exchange to consume messages

---

### Quick and dirty test

The following class can be run as a subscriber which will receive published stock messages and print them to the console:

(HINTS: `docker-compose -f docker-compose.yml -f docker-compose.stocktrader.yml up -d --build` should have executed without errors and `RABBITMQ_EXCHANGE` is set to "stocks" in this example)

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