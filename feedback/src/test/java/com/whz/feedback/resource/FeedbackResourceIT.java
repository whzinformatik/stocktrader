/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.resource;

import static org.hamcrest.Matchers.*;

import com.whz.feedback.infrastructure.FeedbackData;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class FeedbackResourceIT extends ResourceTestCase {

  @Test
  public void testReady() {
    givenJsonClient().when().get("/ready").then().statusCode(200);
  }

  @Test
  public void testPost() {
    createFeedback("message")
        .then()
        .statusCode(201)
        .body("id", notNullValue())
        .body("message", equalTo("message"));
  }

  @Test
  public void testGet() {
    FeedbackData data = createFeedback("test").getBody().as(FeedbackData.class);

    givenJsonClient()
        .when()
        .get("/" + data.id)
        .then()
        .statusCode(200)
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
        .statusCode(200)
        .body("id", contains(data1.id, data2.id))
        .body("message", contains(data1.message, data2.message));
  }

  private Response createFeedback(String message) {
    return givenJsonClient().body(FeedbackData.just(message)).when().post("/");
  }
}
