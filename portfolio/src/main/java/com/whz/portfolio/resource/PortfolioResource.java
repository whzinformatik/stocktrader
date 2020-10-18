package com.whz.portfolio.resource;

import io.vlingo.actors.Stage;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;
import static io.vlingo.http.resource.ResourceBuilder.resource;


public class PortfolioResource extends ResourceHandler {

  private final Stage stage;

  public PortfolioResource(final Stage stage) {
      this.stage = stage;
  }


  @Override
  public Resource<?> routes() {
     return resource("PortfolioResource" /*Add Request Handlers here as a second parameter*/);
  }


}