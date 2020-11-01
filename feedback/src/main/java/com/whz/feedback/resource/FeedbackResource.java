package com.whz.feedback.resource;

import com.whz.feedback.infrastructure.FeedbackData;
import com.whz.feedback.model.feedback.Feedback;
import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;
import static io.vlingo.http.resource.ResourceBuilder.*;

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

  public Completes<Response> create(FeedbackData feedbackData){
    return Feedback.defineWith(stage, feedbackData.message)
            .andThenTo(state -> Completes.withSuccess(Response.of(Created, headers(of(Location, location(state.id))).and(of(ContentType, "application/json")), serialized(FeedbackData.from(state)))));
  }

  @Override
  public Resource<?> routes() {
    logger.info("calling ready...");
    return resource(getClass().getSimpleName(),
            get("/ready").handle(this::ready),
            post("/").body(FeedbackData.class).handle(this::create));
  }

  private String location(String id){
  return "/" + id;
  }
}
