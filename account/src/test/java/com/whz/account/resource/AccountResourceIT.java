/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.whz.account.infrastructure.AccountData;
import io.restassured.response.Response;
import io.vlingo.http.Response.Status;
import org.junit.jupiter.api.Test;

public class AccountResourceIT extends ResourceTestCase {

  @Test
  public void testReady() {
    givenJsonClient().when().get("/ready").then().statusCode(Status.Ok.code);
  }

  @Test
  public void testPost() {
    double balance = 40_000d;
    AccountData result =
        createAccount(balance)
            .then()
            .statusCode(Status.Created.code)
            .extract()
            .body()
            .as(AccountData.class);

    assertNotNull(result.id);
    assertEquals(balance, result.balance);
  }

  @Test
  public void testGet() {
    AccountData data = createAccount(40000d).getBody().as(AccountData.class);
    AccountData result =
        givenJsonClient()
            .when()
            .get("/account/" + data.id)
            .then()
            .statusCode(Status.Ok.code)
            .extract()
            .body()
            .as(AccountData.class);

    assertEquals(data.id, result.id);
    assertEquals(data.balance, result.balance);
  }

  private Response createAccount(double balance) {
    return givenJsonClient().body("{\"balance\": " + balance + "}").when().post("/account");
  }
}
