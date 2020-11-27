/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.resource;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;
import static io.vlingo.http.resource.ResourceBuilder.*;

import com.whz.feedback.infrastructure.FeedbackData;
import com.whz.feedback.infrastructure.persistence.FeedbackQueries;
import com.whz.feedback.infrastructure.persistence.QueryModelStateStoreProvider;
import com.whz.feedback.model.feedback.Feedback;
import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;

public class FeedbackResource extends ResourceHandler {

  private final Stage stage;
  private final FeedbackQueries queries;

  private final Logger logger;

  public FeedbackResource(final Stage stage) {
    this.stage = stage;
    this.queries = QueryModelStateStoreProvider.instance().feedbackQueries;
    this.logger = stage.world().defaultLogger();
  }

  public Completes<Response> ready() {
    return Completes.withSuccess(Response.of(Response.Status.Ok));
  }

  public Completes<Response> create(FeedbackData feedbackData) {
    return Feedback.defineWith(stage, feedbackData.message)
        .andThenTo(
            state ->
                Completes.withSuccess(
                    Response.of(
                        Created,
                        headers(of(Location, location(state.id)))
                            .and(of(ContentType, "application/json")),
                        serialized(FeedbackData.from(state)))));
  }

  public Completes<Response> queryFeedback(String feedbackId) {
    return queries
        .feedbackOf(feedbackId)
        .andThenTo(
            FeedbackData.empty(),
            data ->
                Completes.withSuccess(
                    Response.of(
                        Ok, headers(of(ContentType, "application/json")), serialized(data))))
        .otherwise(noData -> Response.of(NotFound, location(feedbackId)));
  }

  public Completes<Response> queryFeedbacks() {
    return queries
        .feedbacks()
        .andThenTo(
            data ->
                Completes.withSuccess(
                    Response.of(
                        Ok, headers(of(ContentType, "application/json")), serialized(data))));
  }

  @Override
  public Resource<?> routes() {
    logger.info("calling ready...");
    return resource(
        getClass().getSimpleName(),
        get("/ready").handle(this::ready),
        post("/").body(FeedbackData.class).handle(this::create),
        get("/{feedbackId}").param(String.class).handle(this::queryFeedback),
        get("/").handle(this::queryFeedbacks));
  }

  private String location(String id) {
    return "/" + id;
  }
}
