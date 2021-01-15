/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.resource;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.NotFound;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.ResponseHeader.ContentType;
import static io.vlingo.http.ResponseHeader.Location;
import static io.vlingo.http.ResponseHeader.headers;
import static io.vlingo.http.ResponseHeader.of;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.post;
import static io.vlingo.http.resource.ResourceBuilder.resource;

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

/**
 * This class is used as the rest resource for all feedback messages.
 *
 * @since 1.0.0
 */
public class FeedbackResource extends ResourceHandler {

  private static final String APPLICATION_JSON = "application/json";

  private final Stage stage;

  private final FeedbackQueries queries;

  private final Logger logger;

  /**
   * Create an instance of the rest resource.
   *
   * @param stage stage of the current world
   * @since 1.0.0
   */
  public FeedbackResource(final Stage stage) {
    this.stage = stage;
    this.queries = QueryModelStateStoreProvider.instance().feedbackQueries;
    this.logger = stage.world().defaultLogger();
  }

  /**
   * GET-Request to test if the service is ready to do something.
   *
   * @return response for an asynchronous call with a potential result
   * @since 1.0.0
   */
  public Completes<Response> ready() {
    return Completes.withSuccess(Response.of(Response.Status.Ok));
  }

  /**
   * POST-request to create a new feedback message.
   *
   * @param feedbackData feedback message of the user
   * @return response for an asynchronous call with a potential result
   * @since 1.0.0
   */
  public Completes<Response> create(FeedbackData feedbackData) {
    return Feedback.defineWith(stage, feedbackData.message, feedbackData.portfolioId)
        .andThenTo(
            state ->
                Completes.withSuccess(
                    Response.of(
                        Created,
                        headers(of(Location, location(state.id)))
                            .and(of(ContentType, APPLICATION_JSON)),
                        serialized(FeedbackData.from(state)))));
  }

  /**
   * GET-Request to get the details about a specific feedback message.
   *
   * @param feedbackId id of the feedback message sent
   * @return response for an asynchronous call with a potential result
   * @since 1.0.0
   */
  public Completes<Response> queryFeedback(String feedbackId) {
    return queries
        .feedbackOf(feedbackId)
        .andThenTo(
            FeedbackData.empty(),
            data ->
                Completes.withSuccess(
                    Response.of(Ok, headers(of(ContentType, APPLICATION_JSON)), serialized(data))))
        .otherwise(noData -> Response.of(NotFound, location(feedbackId)));
  }

  /**
   * GET-Request to get the details about all feedback messages.
   *
   * @return response for an asynchronous call with a potential result
   * @since 1.0.0
   */
  public Completes<Response> queryFeedbacks() {
    return queries
        .feedbacks()
        .andThenTo(
            data ->
                Completes.withSuccess(
                    Response.of(Ok, headers(of(ContentType, APPLICATION_JSON)), serialized(data))));
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

  /**
   * Create the response header location for the specific request.
   *
   * @param id identifier of the feedback message
   * @return path for the header location
   * @since 1.0.0
   */
  private String location(String id) {
    return "/" + id;
  }
}
