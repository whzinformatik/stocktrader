package com.whz.feedback.resource;

import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.resource;

import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;

public class FeedbackResource extends ResourceHandler {

  private final Stage stage;

  private final Logger logger;

  public FeedbackResource(final Stage stage) {
    this.stage = stage;
    this.logger = stage.world().defaultLogger();
  }

  public Completes<Response> ready() {
    return Completes.withSuccess(Response.of(Response.Status.Ok));
  }

  @Override
  public Resource<?> routes() {
    logger.info("calling ready...");
    return resource(getClass().getSimpleName(), get("/ready").handle(this::ready));
  }
}
