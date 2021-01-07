# Account

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
* loyalty - Loyalty level to show how loyal you are to the broker (Enum can be "Basic", "Bronze", "Silver", "Gold" and "Platinum")
* commissions - Minor amount of money that is charged each time you buy a stock
* free - Flat amount of stocks you can buy without getting charged a commission
* sentiment - Your sentiment according to the broker (Enum can be "Unknown", "Good" and "Bad")

### Events
* AccountCreated - Creates an Account with a given inital balance
* MoneyInvested - Adds the invested money value to the Account which gets triggered when buying a Stock through the Portfolio microservice.

### API
* /account/{id}  (GET)
* /account       (POST)

### System variables
* PORTFOLIO_URL - The Portfolio Url to send requests which needs to be of format: "http://host:port"
* RABBITMQ_SERVICE - The RabbitMQ service name
* RABBITMQ_EXCHANGE - The Exchange name which will be used for the Queue to link Pub and Sub
* RABBITMQ_EXCHANGE_TYPE - The Exchange Type; can be one of direct, fanout, topic and headers