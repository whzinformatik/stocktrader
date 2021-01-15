# Account

![account](https://github.com/whzinformatik/stocktrader/workflows/account/badge.svg)

This project is used to create and manage an account in the stock trader application. It communicates with the comment tone and the portfolio project.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Installing

In order to get an executable environment for development, some environment variables have to be set first:

| Variable                        | Description                                              | Default Value     |
|---------------------------------|----------------------------------------------------------|-------------------|
| PORTFOLIO_URL                   | to send Portfolio-Requests of format: "http://host:port" | localhost:18082   |
| RABBITMQ_SERVICE                | RabbitMQ service name                                    | localhost         |
| RABBITMQ_EXCHANGE_STOCKACQUIRED | used for Queue to link Sub with StockAcquiredPublisher   | stock_logs        |
| RABBITMQ_EXCHANGE_COMMENTTONE   | used for Queue to link Sub with CommentTonePublisher     | comment_tone_logs |
| RABBITMQ_EXCHANGE_TYPE          | can be one of direct, fanout, topic and headers          | fanout            |

Make sure that the local environment is active. Then the program can be executed using the following command:

```bash
mvn exec:exec
```

At the end, you can send a simple REST request to create an account for the stock trader application:

```bash
curl --location --request POST 'localhost:18084/account' \
--header 'Content-Type: application/json' \
--data-raw '{
    "balance": 40000.0
}'
```

## Documentation

You can get more details about the project with the documented code or the domain stories in the [`doc`][documentation] folder.

[account_actions]: https://github.com/whzinformatik/stocktrader/actions?query=workflow%3Aaccount

### Resources
It is part of the: [IBMStockTrader Java EE Portfolio Implementation](https://github.com/IBMStockTrader/portfolio/blob/master/src/main/java/com/ibm/hybrid/cloud/sample/stocktrader/portfolio/json/Portfolio.java).

In our new project, the Portfolio will be split up into a separate "Portfolio" and "Account" Microservice.
The Account will keep all the information that is not of interest to the Portfolio like balance details and broker related information.

[Vlingo docs](https://docs.vlingo.io/)

### Data Structure
	public class Account {
	   public String id;
	   public double balance;
	   public double totalInvested;
	   public Loyalty loyalty;
	   public double commissions;
	   public int free;
	   public Sentiment sentiment;
    }

### Data fields
* id - Default generated
* balance - An account will be created with a given balance; Balance will be used when buying stocks
* totalInvested - Based on the total invested amount of Money your loyalty level will rise
* loyalty - Loyalty level to show how loyal you are to the broker (Enum can be "BASIC", "BRONZE", "SILVER", "GOLD" and "PLATINUM")
* commissions - Minor amount of money that is charged each time you buy a stock
* free - Flat amount of stocks you can buy without getting charged a commission
* sentiment - Your sentiment according to the broker (Enum can be "UNKNOWN", "POSITIVE", "NEUTRAL" and "NEGATIVE")

### Events
* AccountCreated - Creates an Account with a given inital balance
* MoneyInvested - Adds the invested money value to the Account which gets triggered when buying a stock through the Portfolio microservice.
* SentimentReceived - Changes the sentiment value to the Account which gets triggered when receiving an assessment from the CommentTone microservice.

### API
* /account/{id}  (GET)
* /account       (POST)