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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.whz.feedback.infrastructure.Bootstrap;
import com.whz.feedback.infrastructure.persistence.CommandModelJournalProvider;
import com.whz.feedback.infrastructure.persistence.ProjectionDispatcherProvider;
import com.whz.feedback.infrastructure.persistence.QueryModelStateStoreProvider;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

abstract class ResourceTestCase {

  private static final AtomicInteger portNumberPool = new AtomicInteger(18080);

  private int portNumber;

  private Bootstrap bootstrap;

  @BeforeEach
  public void setup() throws Exception {
    portNumber = portNumberPool.getAndIncrement();
    bootstrap = new Bootstrap(portNumber);
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
    return given().port(portNumber).accept(ContentType.JSON).contentType(ContentType.JSON);
  }
}
