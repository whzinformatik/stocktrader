package com.whz.feedback.resource;

import static org.hamcrest.Matchers.*;

import com.whz.feedback.infrastructure.FeedbackData;
import org.junit.jupiter.api.Test;

public class FeedbackResourceTest extends ResourceTestCase {

  @Test
  public void testReady() {
    givenJsonClient().when().get("/ready").then().statusCode(200);
  }

  @Test
  public void testPost() {
    FeedbackData data = FeedbackData.just("message");
    givenJsonClient()
        .body(data)
        .when()
        .post("/")
        .then()
        .statusCode(201)
        .body("id", notNullValue())
        .body("message", equalTo("message"));
  }
}
