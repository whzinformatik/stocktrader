package com.whz.account.resource;

import io.vlingo.actors.Stage;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;
import static io.vlingo.http.resource.ResourceBuilder.resource;


public class AccountResource extends ResourceHandler {

  private final Stage stage;

  public AccountResource(final Stage stage) {
      this.stage = stage;
  }


  @Override
  public Resource<?> routes() {
     return resource("AccountResource" /*Add Request Handlers here as a second parameter*/);
  }


}