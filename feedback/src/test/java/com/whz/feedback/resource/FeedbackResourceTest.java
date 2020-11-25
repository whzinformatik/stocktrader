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

import static org.hamcrest.Matchers.*;

import com.whz.feedback.infrastructure.FeedbackData;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class FeedbackResourceTest extends ResourceTestCase {

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
