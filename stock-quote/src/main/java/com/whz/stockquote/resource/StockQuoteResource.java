package com.whz.stockquote.resource;

import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.Map;

import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.resource;

public class StockQuoteResource extends ResourceHandler {

    private final Stage stage;
    private final Logger logger;

    public StockQuoteResource(final Stage stage) {
        this.stage = stage;
        this.logger = stage.world().defaultLogger();
    }

    public Completes<Response> retrieve() {
        try {
            Stock stock = YahooFinance.get("NRG");
            String answer = stock.getSymbol() + ": " + stock.getQuote().getPrice().toString();
            logger.info("RETURN STOCK PRICE\n");
            return Completes.withSuccess(Response.of(Response.Status.Ok, "Retrieved: \n" + answer));
        } catch (IOException e) {
            e.printStackTrace();
            return Completes.withFailure(Response.of(Response.Status.NotImplemented));
        }
    }

    public Completes<Response> retrieveMulti() {
        try {
            StringBuilder b = new StringBuilder();

            String[] symbols = new String[]{"INTC", "BABA", "TSLA", "AIR.PA", "AAPL", "MSFT", "NRG", "ZM"};
            Map<String, Stock> stocks = YahooFinance.get(symbols); // single request
            stocks.forEach((key, value) -> b.append(value).append("\n"));
            String answer = b.toString();

            logger.info("RETURN STOCK PRICES\n");
            return Completes.withSuccess(Response.of(Response.Status.Ok, "Retrieved: \n" + answer));
        } catch (IOException e) {
            e.printStackTrace();
            return Completes.withFailure(Response.of(Response.Status.NotImplemented));
        }
    }

    @Override
    public Resource<?> routes() {
        return resource("StockQuoteResource", //
                get("/retrieve").handle(this::retrieve), //
                get("/retrieveMulti").handle(this::retrieveMulti)
        );
    }
}