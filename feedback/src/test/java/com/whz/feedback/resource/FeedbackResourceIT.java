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

import com.whz.feedback.infrastructure.FeedbackData;
import com.whz.feedback.model.feedback.FeedbackActor;
import com.whz.feedback.model.feedback.FeedbackState;
import com.whz.feedback.utils.EnvUtils;
import io.restassured.response.Response;
import io.vlingo.http.Response.Status;
import java.io.IOException;
import java.util.List;
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
    TestSubscriber<FeedbackState> subscriber =
        new TestSubscriber<>(EnvUtils.RABBITMQ_SERVICE.get());
    subscriber.receive(FeedbackActor.EXCHANGE_NAME, FeedbackState.class);

    String message = "message";
    createFeedback(message)
        .then()
        .statusCode(Status.Created.code)
        .body("id", notNullValue())
        .body("message", equalTo(message));

    await().until(subscriber::containsData);

    List<FeedbackState> messages = subscriber.getMessages();
    assertThat(messages).isNotEmpty();

    FeedbackState state = subscriber.lastMessage();
    assertThat(state.id).isNotNull();
    assertThat(state.message).isEqualTo(message);
  }

  @Test
  public void testGet() {
    FeedbackData data = createFeedback("test").getBody().as(FeedbackData.class);

    givenJsonClient()
        .when()
        .get("/" + data.id)
        .then()
        .statusCode(Status.Ok.code)
        .body("id", equalTo(data.id))
        .body("message", equalTo(data.message));
  }

  @Test
  public void testGetAll() {
    FeedbackData data1 = createFeedback("test1").getBody().as(FeedbackData.class);
    FeedbackData data2 = createFeedback("test2").getBody().as(FeedbackData.class);

    givenJsonClient()
        .when()
        .get("/")
        .then()
        .statusCode(Status.Ok.code)
        .body("id", contains(data1.id, data2.id))
        .body("message", contains(data1.message, data2.message));
  }

  private Response createFeedback(String message) {
    return givenJsonClient().body(FeedbackData.just(message)).when().post("/");
  }
}
