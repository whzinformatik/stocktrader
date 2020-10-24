package com.whz.feedback.resource;

import org.junit.jupiter.api.Test;

public class FeedbackResourceTest extends ResourceTestCase {

  @Test
  public void testReady() {
    givenJsonClient().when().get("/ready").then().statusCode(200);
  }
}
