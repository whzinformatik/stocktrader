package com.whz.portfolio.resource;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;

import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.resource;

public class PortfolioResource extends ResourceHandler {

	private final Stage stage;

	public PortfolioResource(final Stage stage) {
		this.stage = stage;
	}

	public Completes<Response> create() {
		return Completes.withSuccess(Response.of(Ok, "Created"));
	}

	public Completes<Response> retrieve(String id) {
		return Completes.withSuccess(Response.of(Ok, "Retrieved: " + id));
	}

	public Completes<Response> update() {
		return Completes.withSuccess(Response.of(Ok, "Updated"));
	}

	public Completes<Response> delete() {
		return Completes.withSuccess(Response.of(Ok, "Deleted"));
	}

	@Override
	public Resource<?> routes() {
		return resource("PortfolioResource", //
				get("/create").handle(this::create), //
				get("/retrieve/{id}").param(String.class).handle(this::retrieve), //
				get("/update").handle(this::update), //
				get("/delete").handle(this::delete) //
		);
	}

}