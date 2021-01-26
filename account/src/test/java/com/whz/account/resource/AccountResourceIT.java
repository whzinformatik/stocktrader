/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.resource;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.whz.account.infrastructure.AccountData;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class AccountResourceIT extends ResourceTestCase {

  @Test
  public void testReady() {
    givenJsonClient().when().get("/ready").then().statusCode(200);
  }

  @Test
  public void testPost() {
    createAccount(40000d)
        .then()
        .statusCode(201)
        .body("id", notNullValue())
        .body("balance", equalTo(40000d));
  }

  @Test
  public void testGet() {
    AccountData data = createAccount(40000d).getBody().as(AccountData.class);

    givenJsonClient()
        .when()
        .get("/account/" + data.id)
        .then()
        .statusCode(200)
        .body("id", equalTo(data.id))
        .body("balance", equalTo(data.balance));
  }

  private Response createAccount(double balance) {
    return givenJsonClient().body("{\"balance\": " + balance + "}").when().post("/account");
  }
}
