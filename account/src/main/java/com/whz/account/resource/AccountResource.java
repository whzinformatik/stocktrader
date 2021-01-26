/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.resource;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.NotFound;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.ResponseHeader.ContentType;
import static io.vlingo.http.ResponseHeader.Location;
import static io.vlingo.http.ResponseHeader.headers;
import static io.vlingo.http.ResponseHeader.of;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.post;
import static io.vlingo.http.resource.ResourceBuilder.resource;

import com.whz.account.infrastructure.AccountData;
import com.whz.account.infrastructure.persistence.AccountQueries;
import com.whz.account.infrastructure.persistence.QueryModelStateStoreProvider;
import com.whz.account.model.account.Account;
import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * The AccountResource is responsible for managing the REST interface and invoking methods/events.
 *
 * @since 1.0.0
 */
public class AccountResource extends ResourceHandler {

  private Logger logger;

  private final Stage stage;
  private final AccountQueries accountQueries;
  private final String portfolioUrl =
      Optional.ofNullable(System.getenv("PORTFOLIO_URL")).orElse("http://localhost:18082");

  /**
   * Sets the stage and accountQueries and initializes a StockAcquiredSubscriber singleton.
   *
   * @since 1.0.0
   * @param stage - stage object of the world
   */
  public AccountResource(final Stage stage) {
    StockAcquiredSubscriber.INSTANCE.setStage(stage);
    CommentToneSubscriber.INSTANCE.setStage(stage);

    this.logger = stage.world().defaultLogger();

    this.stage = stage;
    this.accountQueries = QueryModelStateStoreProvider.instance().accountQueries;
  }

  /**
   * Get request method which returns an AccountData object based on a given id.
   *
   * @since 1.0.0
   * @param id - the id of the AccountData object that gets requested
   * @return Response - an HTTP answer (JSON) with the AccountData object representing an Account
   */
  public Completes<Response> handleGetAccount(String id) {
    return accountQueries
        .accountOf(id)
        .andThenTo(
            AccountData.empty(),
            data -> {
              data.balance = Math.round(data.balance * 100d) / 100d;
              data.totalInvested = Math.round(data.totalInvested * 100d) / 100d;

              return Completes.withSuccess(
                  Response.of(Ok, headers(of(ContentType, "application/json")), serialized(data)));
            })
        .otherwise(noData -> Response.of(NotFound, location(id)));
  }

  /**
   * Post method which creates an Account with a selfdefined id and given balance. It accepts a Json
   * format containing a "balance":double value. While creating an Event to generate an Account it
   * also triggers a call to a REST interface of the Portfolio microservice which creates a
   * corresponding Portfolio to our Account in a 1:1 relation based on their id's.
   *
   * @since 1.0.0
   * @param data - an AccountData object representing an Account object
   * @return Response - an HTTP answer (JSON) which contains the information if the Account was
   *     successfully created
   */
  public Completes<Response> handlePostAccount(AccountData data) {
    return Account.defineWith(stage, data.balance)
        .andThenTo(
            state -> {
              createPortfolioRequest(state.id);
              return Completes.withSuccess(
                  Response.of(
                      Created,
                      headers(of(Location, location(state.id)))
                          .and(of(ContentType, "application/json")),
                      serialized(AccountData.from(state))));
            });
  }

  /**
   * GET-Request to test if the service is ready to do something.
   *
   * @since 1.0.0
   * @return response for an asynchronous call with a potential result
   */
  public Completes<Response> ready() {
    return Completes.withSuccess(Response.of(Response.Status.Ok));
  }

  /**
   * Maps the HTTP-Requests to the corresponding handler method.
   *
   * @since 1.0.0
   */
  @Override
  public Resource<?> routes() {
    return resource(
        getClass().getSimpleName(),
        get("/ready").handle(this::ready),
        get("/account/{id}").param(String.class).handle(this::handleGetAccount),
        post("/account").body(AccountData.class).handle(this::handlePostAccount));
  }

  /**
   * Parses a String to represent our account id path.
   *
   * @since 1.0.0
   * @param id - the id of the AccountState object
   * @return String - the parsed id
   */
  private String location(final String id) {
    return "/account/" + id;
  }

  /**
   * The full HTTP POST-Request call to the Portfolio microservice which will create a Portfolio to
   * our Account in a 1:1 relation based on the given id.
   *
   * @since 1.0.0
   * @param id - the id which will be shared between an Account and a Portfolio
   */
  private void createPortfolioRequest(String id) {
    String requestBody = id;

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(portfolioUrl + "/portfolio"))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

    try {
      client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      logger.error("Unable to send HTTP Request", e);
    }
  }
}
