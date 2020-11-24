# Portfolio

### Resources
[IBMStockTrader Java EE Portfolio Implementation](https://github.com/IBMStockTrader/portfolio/blob/master/src/main/java/com/ibm/hybrid/cloud/sample/stocktrader/portfolio/json/Portfolio.java)

[Vlingo docs](https://docs.vlingo.io/)

### Data Structure
	public class Portfolio {
		private String owner;
		private double total;
	    private String loyalty;
	    private double balance;
	    private double commissions;
	    private int free;
	    private String sentiment;
	    private double nextCommission;
	    private List<String> stockIds;
    }


### Events
* PortfolioRetrieved
* PortfolioCreated
* PortfolioUpdated
* PortfolioDeleted

### API
* /portfolio/{id}	(GET)
* /portfolio		(POST)
* /portfolio		(PUT)
* /portfolio/{id}	(DELETE)
* /stocks			(GET)
* /stocks{id}		(GET)