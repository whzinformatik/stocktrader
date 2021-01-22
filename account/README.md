# Account

[![account](https://github.com/whzinformatik/stocktrader/workflows/account/badge.svg)][account_actions]

This project is used to create and manage an account in the stock trader application. It communicates with the comment tone and the portfolio project.

It is part of the: [IBMStockTrader Java EE Portfolio Implementation](https://github.com/IBMStockTrader/portfolio/blob/master/src/main/java/com/ibm/hybrid/cloud/sample/stocktrader/portfolio/json/Portfolio.java).

In our new project, the Portfolio will be split up into a separate "Portfolio" and "Account" Microservice.
The Account will keep all the information that is not of interest to the Portfolio like balance details and broker related information.

[Vlingo docs](https://docs.vlingo.io/)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Installing

In order to get an executable environment for development, some environment variables have to be set first:

| Variable                        | Description                                              | Default Value          |
|---------------------------------|----------------------------------------------------------|------------------------|
| PORTFOLIO_URL                   | to send Portfolio-Requests of format: "http://host:port" | http://localhost:18082 |
| RABBITMQ_SERVICE                | RabbitMQ service name                                    | localhost              |
| RABBITMQ_EXCHANGE_STOCKACQUIRED | used for Queue to link Sub with StockAcquiredPublisher   | stocks-acquired        |
| RABBITMQ_EXCHANGE_COMMENTTONE   | used for Queue to link Sub with CommentTonePublisher     | commentTone            |
| RABBITMQ_EXCHANGE_TYPE          | can be one of direct, fanout, topic and headers          | fanout                 |

Make sure that the local environment is active. Then the program can be executed using the following command:

```bash
mvn exec:exec
```

At the end, you can send a simple REST request to create an account for the stock trader application:

```bash
curl --location --request POST 'localhost:18081/account' \
--header 'Content-Type: application/json' \
--data-raw '{
    "balance": 40000.0
}'
```

[account_actions]: https://github.com/whzinformatik/stocktrader/actions?query=workflow%3Aaccount
