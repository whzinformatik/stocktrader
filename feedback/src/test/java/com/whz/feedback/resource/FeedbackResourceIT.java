/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.whz.feedback.exchange.FeedbackDTO;
import com.whz.feedback.infrastructure.FeedbackData;
import com.whz.feedback.model.feedback.FeedbackActor;
import com.whz.feedback.model.feedback.FeedbackState;
import com.whz.feedback.utils.EnvUtils;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.vlingo.http.Response.Status;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FeedbackResourceIT extends ResourceTestCase {

  @Test
  public void testReady() {
    givenJsonClient().when().get("/ready").then().statusCode(Status.Ok.code);
  }

  @Test
  public void testPost() throws IOException, TimeoutException {
    TestSubscriber<FeedbackDTO> subscriber =
        new TestSubscriber<>(EnvUtils.RABBITMQ_SERVICE.get());
    subscriber.receive(FeedbackActor.EXCHANGE_NAME, FeedbackDTO.class);

    String message = "message";
    String portfolioId = "portfolio_id";
    createFeedback(message, portfolioId)
        .then()
        .statusCode(Status.Created.code)
        .body("id", notNullValue())
        .body("message", equalTo(message))
        .body("portfolioId", equalTo(portfolioId));

    await().until(subscriber::containsData);

    FeedbackDTO feedbackDTO = subscriber.lastMessage();
    assertThat(feedbackDTO.id).isNotNull();
    assertThat(feedbackDTO.message).isEqualTo(message);
  }

  @Test
  public void testGet() {
    FeedbackData data = createFeedback("test", "pid").getBody().as(FeedbackData.class);

    givenJsonClient()
        .when()
        .get("/" + data.id)
        .then()
        .statusCode(Status.Ok.code)
        .body("id", equalTo(data.id))
        .body("message", equalTo(data.message))
        .body("portfolioId", equalTo(data.portfolioId));
  }

  @Test
  public void testGetAll() {
    FeedbackData data1 = createFeedback("test1", "pid").getBody().as(FeedbackData.class);
    FeedbackData data2 = createFeedback("test2", "pid").getBody().as(FeedbackData.class);

    givenJsonClient()
        .when()
        .get("/")
        .then()
        .statusCode(Status.Ok.code)
        .body("id", contains(data1.id, data2.id))
        .body("message", contains(data1.message, data2.message))
        .body("portfolioId", contains(data1.portfolioId, data2.portfolioId));
  }

  private Response createFeedback(String message, String portfolioId) {
    return givenJsonClient().body(FeedbackData.just(message, portfolioId)).when().post("/");
  }
}
