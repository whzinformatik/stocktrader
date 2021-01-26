/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.resource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.whz.account.infrastructure.Bootstrap;
import com.whz.account.infrastructure.persistence.CommandModelJournalProvider;
import com.whz.account.infrastructure.persistence.ProjectionDispatcherProvider;
import com.whz.account.infrastructure.persistence.QueryModelStateStoreProvider;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

abstract class ResourceTestCase {

  private static final int PORT = 18081;

  private Bootstrap bootstrap;

  @BeforeEach
  public void setup() throws Exception {
    bootstrap = new Bootstrap(PORT);
    boolean startupSuccessful = bootstrap.getServer().startUp().await(100);
    assertTrue(startupSuccessful);
  }

  @AfterEach
  public void cleanup() {
    bootstrap.getServer().stop();

    QueryModelStateStoreProvider.reset();
    ProjectionDispatcherProvider.reset();
    CommandModelJournalProvider.reset();
  }

  protected RequestSpecification givenJsonClient() {
    return given().port(PORT).accept(ContentType.JSON).contentType(ContentType.JSON);
  }
}
