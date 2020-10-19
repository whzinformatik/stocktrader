package com.whz.stock-quote.resource;

import io.vlingo.actors.Stage;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;
import static io.vlingo.http.resource.ResourceBuilder.resource;


public class StockQuoteResource extends ResourceHandler {

  private final Stage stage;

  public StockQuoteResource(final Stage stage) {
      this.stage = stage;
  }


  @Override
  public Resource<?> routes() {
     return resource("StockQuoteResource" /*Add Request Handlers here as a second parameter*/);
  }


}