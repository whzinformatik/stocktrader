# Account

### Resources
It is part of the: [IBMStockTrader Java EE Portfolio Implementation](https://github.com/IBMStockTrader/portfolio/blob/master/src/main/java/com/ibm/hybrid/cloud/sample/stocktrader/portfolio/json/Portfolio.java).

In our new project, the Portfolio will be split up into a separate "Portfolio" and "Account" Microservice.
The Account will keep all the information that is not of interest to the portfolio like balance details and broker related information.

[Vlingo docs](https://docs.vlingo.io/)

### Data Structure
	public class Account {
	   public String id;
	   public double balance;
	   public double totalInvested;
	   public String loyalty;
	   public double commissions;
	   public int free;
	   public String sentiment;
    }

### Data fields
* id - Default generated
* balance - An account will be created with a given balance; Balance will be used when buying stocks
* totalInvested - Based on the total invested amount of Money your loyalty level will rise
* loyalty - Loyalty level to show how loyal you are to the broker
* commissions - Minor amount of money that is charged each time you buy a stock
* free - Flat amount of stocks you can buy without getting charged a commission
* sentiment - Your sentiment according to the broker

### Events
* AccountCreated - Creates an Account with a given inital balance
* AccountRetrieved - Retrieves an Account based on its id

### API
* /portfolio/{id}	(GET)
* /portfolio		(POST)