# Stock-Quote

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