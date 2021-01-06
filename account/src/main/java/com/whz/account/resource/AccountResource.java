/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <lationts@gmail.com>
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
import com.whz.account.model.account.AccountEntity;
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

public class AccountResource extends ResourceHandler {

  private Logger logger;

  private final Stage stage;
  private final AccountQueries accountQueries;
  private final String portfolioUrl =
      System.getenv("PORTFOLIO_URL"); // TODO http://localhost:18082 <-- readme

  public AccountResource(final Stage stage) {
    StockAcquiredSubscriber saSub = StockAcquiredSubscriber.INSTANCE;
    saSub.setStage(stage);

    this.logger = stage.world().defaultLogger();

    this.stage = stage;
    this.accountQueries = QueryModelStateStoreProvider.instance().accountQueries;
  }

  // GET
  public Completes<Response> handleGetAccount(String id) {
    return accountQueries
        .accountOf(id)
        .andThenTo(
            AccountData.empty(),
            data ->
                Completes.withSuccess(
                    Response.of(
                        Ok, headers(of(ContentType, "application/json")), serialized(data))))
        .otherwise(noData -> Response.of(NotFound, location(id)));
  }

  // POST
  public Completes<Response> handlePostAccount(AccountData data) {
    return Account.defineWith(stage, data.balance)
        .andThenTo(
            state -> {
              createPortfolio(state.id);
              return Completes.withSuccess(
                  Response.of(
                      Created,
                      headers(of(Location, location(state.id)))
                          .and(of(ContentType, "application/json")),
                      serialized(AccountData.from(state))));
            });
  }

  @Override
  public Resource<?> routes() {
    return resource(
        getClass().getSimpleName(),
        get("/account/{id}").param(String.class).handle(this::handleGetAccount),
        post("/account").body(AccountData.class).handle(this::handlePostAccount));
  }

  private String location(final String id) {
    return "/account/" + id;
  }

  private Completes<Account> resolve(final String id) {
    return stage.actorOf(Account.class, stage.addressFactory().from(id), AccountEntity.class);
  }

  private void createPortfolio(String id) {
    String requestBody = id;

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(portfolioUrl + "/portfolio"))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      logger.error("Unable to send HTTP Request", e);
    }
  }
}
