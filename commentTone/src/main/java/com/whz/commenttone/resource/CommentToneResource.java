package com.whz.commenttone.resource;

import io.vlingo.actors.Stage;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;

import static io.vlingo.http.resource.ResourceBuilder.resource;


public class CommentToneResource extends ResourceHandler {

  private final Stage stage;

  public CommentToneResource(final Stage stage) {
      this.stage = stage;
  }


  @Override
  public Resource<?> routes() {
     return resource("CommentToneResource" /*Add Request Handlers here as a second parameter*/);
  }


}
