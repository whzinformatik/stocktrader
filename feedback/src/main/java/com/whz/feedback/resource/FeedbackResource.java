package com.whz.feedback.resource;

/*-
 * #%L
 * feedback
 * %%
 * Copyright (C) 2020 Fachgruppe Informatik WHZ
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
