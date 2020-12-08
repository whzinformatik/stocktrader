/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <lationts@gmail.com>
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
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;

public class AccountResource extends ResourceHandler {

  private final Stage stage;
  private final AccountQueries accountQueries;

  public AccountResource(final Stage stage) {
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
            state ->
                Completes.withSuccess(
                    Response.of(
                        Created,
                        headers(of(Location, location(state.id)))
                            .and(of(ContentType, "application/json")),
                        serialized(AccountData.from(state)))));
  }

  /*
   * // PUT public Completes<Response> handlePut(AccountData data) { return
   * Completes.withSuccess(Response.of(Ok, "Updated")); }
   *
   * // DELETE public Completes<Response> handleDelete(String id) { return
   * Completes.withSuccess(Response.of(Ok, "Deleted")); }
   */

  @Override
  public Resource<?> routes() {
    return resource(
        getClass().getSimpleName(),
        get("/account/{id}").param(String.class).handle(this::handleGetAccount),
        post("/account").body(AccountData.class).handle(this::handlePostAccount));
    // put("/account").body(AccountData.class).handle(this::handlePut),
    // delete("/account/{id}").param(String.class).handle(this::handleDelete));
  }

  private String location(final String id) {
    return "/account/" + id;
  }

  private Completes<Account> resolve(final String id) {
    return stage.actorOf(Account.class, stage.addressFactory().from(id), AccountEntity.class);
  }
}
