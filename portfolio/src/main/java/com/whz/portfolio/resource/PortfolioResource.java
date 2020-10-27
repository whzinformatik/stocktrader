package com.whz.portfolio.resource;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;
import io.vlingo.symbio.store.journal.Journal;

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

import com.whz.portfolio.infrastructure.PortfolioData;
import com.whz.portfolio.infrastructure.persistence.CommandModelJournalProvider;
import com.whz.portfolio.infrastructure.persistence.PortfolioQueries;
import com.whz.portfolio.infrastructure.persistence.QueryModelStateStoreProvider;
import com.whz.portfolio.model.portfolio.Portfolio;
import com.whz.portfolio.model.portfolio.PortfolioEntity;
import com.whz.portfolio.model.portfolio.PortfolioCreated;

public class PortfolioResource extends ResourceHandler {

	private final Stage stage;
	private final PortfolioQueries portfolioQueries;

	public PortfolioResource(final Stage stage) {
		this.stage = stage;
		this.portfolioQueries = QueryModelStateStoreProvider.instance().portfolioQueries;
	}

	// curl -i -X POST -H "Content-Type: application/json" -d '{"id":"","owner":"Florian" }' http://localhost:18082/portfolio/create
	public Completes<Response> create(PortfolioData data) {
		return Portfolio.defineWith(stage, data.owner)
				.andThenTo(state -> Completes.withSuccess(Response.of(Created,
						headers(of(Location, location(state.id))).and(of(ContentType, "application/json")),
						serialized(PortfolioData.from(state)))));
	}

	// curl -i -X GET http://localhost:18082/portfolio/retrieve/12
	public Completes<Response> retrieve(String id) {
		return portfolioQueries.portfolioOf(id)
				.andThenTo(PortfolioData.empty(),
						data -> Completes.withSuccess(
								Response.of(Ok, headers(of(ContentType, "application/json")), serialized(data))))
				.otherwise(noData -> Response.of(NotFound, location(id)));
	}

	public Completes<Response> update() {
		return Completes.withSuccess(Response.of(Ok, "Updated"));
	}

	public Completes<Response> delete() {
		return Completes.withSuccess(Response.of(Ok, "Deleted"));
	}

	@Override
	public Resource<?> routes() {
		return resource("Portfolio Resource", //
				post("/portfolio/create").body(PortfolioData.class).handle(this::create), //
				get("/portfolio/retrieve/{id}").param(String.class).handle(this::retrieve), //
				get("/portfolio/update").handle(this::update), //
				get("/portfolio/delete").handle(this::delete) //
		);
	}

	private String location(final String id) {
		return "/portfolio/" + id;
	}

	private Completes<Portfolio> resolve(final String id) {
		return stage.actorOf(Portfolio.class, stage.addressFactory().from(id), PortfolioEntity.class);
	}

}