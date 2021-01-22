# Stock-Quote

[![stock-quote](https://github.com/whzinformatik/stocktrader/workflows/stock-quote/badge.svg)][stock-quote_actions]

This project is used to retrieve information about stocks which can be used later in the portfolio project.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Installing

In order to get an executable environment for development, some environment variables have to be set first:

| Variable                | Description                                                                                                                                | Default Value                                                              |
|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------|
| RABBITMQ_SERVICE        | default host to use for rabbitmq connections                                                                                               | localhost                                                                  |
| RABBITMQ_EXCHANGE       | default exchange to use for publishing stock information                                                                                   | stocks                                                                     |
| DURABLE_EXCHANGE        | durability of the exchange (may be true or false)                                                                                          | false                                                                      |
| PUBLISH_INTERVAL        | fixed interval for publishing stock information (in seconds)                                                                               | 3                                                                          |
| RANDOM_PUBLISH_INTERVAL | upper time limit in which messages are published at random intervals (in seconds)<br> NOTE: this variable will override `PUBLISH_INTERVAL` | 0                                                                          |
| STOCK_SYMBOLS           | comma separated list of official stock symbols <br> NOTE: invalid symbols will be ignored                                                  | TSLA,INTC,GOOG,<br>MSFT,AMZN,NVDA,<br>NFLX,SBUX,SAP,<br>ADDYY,POAHF,SAP.DE |


Make sure that the local environment is active. Then the program can be executed using the following command:

```bash
mvn exec:exec
```

[stock-quote_actions]: https://github.com/whzinformatik/stocktrader/actions?query=workflow%3Astock-quote
