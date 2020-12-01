package com.whz.account.resource;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.NotFound;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.ResponseHeader.ContentType;
import static io.vlingo.http.ResponseHeader.Location;
import static io.vlingo.http.ResponseHeader.headers;
import static io.vlingo.http.ResponseHeader.of;
import static io.vlingo.http.resource.ResourceBuilder.delete;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.post;
import static io.vlingo.http.resource.ResourceBuilder.put;
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
	public Completes<Response> handleGet(String id) {
		return accountQueries.accountOf(id)
				.andThenTo(AccountData.empty(),
						data -> Completes.withSuccess(
								Response.of(Ok, headers(of(ContentType, "application/json")), serialized(data))))
				.otherwise(noData -> Response.of(NotFound, location(id)));
	}

	// POST
	public Completes<Response> handlePost(AccountData data) {
		return Account.defineWith(stage, data.balance)
				.andThenTo(state -> Completes.withSuccess(Response.of(Created,
						headers(of(Location, location(state.id))).and(of(ContentType, "application/json")),
						serialized(AccountData.from(state)))));
	}

	// PUT
	public Completes<Response> handlePut(AccountData data) {
		return Completes.withSuccess(Response.of(Ok, "Updated"));
	}

	// DELETE
	public Completes<Response> handleDelete(String id) {
		return Completes.withSuccess(Response.of(Ok, "Deleted"));
	}

	@Override
	public Resource<?> routes() {
		return resource("AccountResource", //
				get("/account/{id}") //
						.param(String.class) //
						.handle(this::handleGet), //
				post("/account") //
						.body(AccountData.class) //
						.handle(this::handlePost), //
				put("/account")//
						.body(AccountData.class) //
						.handle(this::handlePut), //
				delete("/account/{id}") //
						.param(String.class) //
						.handle(this::handleDelete) //
		);
	}

	private String location(final String id) {
		return "/account/" + id;
	}

	private Completes<Account> resolve(final String id) {
		return stage.actorOf(Account.class, stage.addressFactory().from(id), AccountEntity.class);
	}

}