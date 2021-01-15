# Portfolio

[![portfolio](https://github.com/whzinformatik/stocktrader/workflows/portfolio/badge.svg)][portfolio_actions]

The project is used to store stock information for the stock trader application.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Installing

In order to get an executable environment for development, some environment variables have to be set first:

| Variable                              | Description                                  | Default Value   |
|---------------------------------------|----------------------------------------------|-----------------|
| RABBITMQ_SERVICE                      | default host to use for rabbitmq connections | localhost       |
| RABBITMQ_STOCK_ACQUIRED_EXCHANGE      | exchange name for stock acquired publisher   | stocks-acquired |
| RABBITMQ_STOCK_ACQUIRED_EXCHANGE_TYPE | exchange type for stock acquired publisher   | fanout          |
| RABBITMQ_STOCK_QUOTE_EXCHANGE         | exchange name for stock quote subscriber     | stocks          |
| RABBITMQ_STOCK_QUOTE_EXCHANGE_TYPE    | exchange type for stock quote subscriber     | fanout          |

Make sure that the local environment is active. Then the program can be executed using the following command:

```bash
mvn exec:exec
```

At the end, you can send a simple REST GET request to test the connection of the portfolio application:

```bash
curl --location --request GET 'localhost:18082/ready'
```

## Running the tests

You can run all the automated tests of the project with the following command:

```bash
mvn verify
```

## Documentation

You can get more details about the project with the documented code or the domain stories in the [`doc`][documentation] folder.

[portfolio_actions]: https://github.com/whzinformatik/stocktrader/actions?query=workflow%3Aportfolio
[documentation]: ./doc









