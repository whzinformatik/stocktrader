package com.whz.portfolio.resource;

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

import com.whz.portfolio.infrastructure.AcquireStockData;
import com.whz.portfolio.infrastructure.PortfolioData;
import com.whz.portfolio.infrastructure.persistence.PortfolioQueries;
import com.whz.portfolio.infrastructure.persistence.QueryModelStateStoreProvider;
import com.whz.portfolio.model.portfolio.Portfolio;
import com.whz.portfolio.model.portfolio.PortfolioEntity;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;

public class PortfolioResource extends ResourceHandler {

	private final Stage stage;
	private final PortfolioQueries portfolioQueries;

	public PortfolioResource(final Stage stage) {
		// access attempt to initialize the singleton
		QuotesCache quotesCache = QuotesCache.INSTANCE;

		this.stage = stage;
		this.portfolioQueries = QueryModelStateStoreProvider.instance().portfolioQueries;
	}

	// get a portfolio
	public Completes<Response> handleGet(String id) {
		return portfolioQueries.portfolioOf(id).andThenTo(PortfolioData.empty(), data -> {
			data.total = 0.0;
			data.stockList.forEach(stock -> {
				data.total += QuotesCache.INSTANCE.get(stock.symbol).regularMarketPrice * stock.amount;
			});
			return Completes
					.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(data)));
		}).otherwise(noData -> Response.of(NotFound, location(id)));
	}

	// create new portfolio
	public Completes<Response> handlePost(String owner) {
		return Portfolio.defineWith(stage, owner)
				.andThenTo(state -> Completes.withSuccess(Response.of(Created,
						headers(of(Location, location(state.id))).and(of(ContentType, "application/json")),
						serialized(PortfolioData.from(state)))));
	}

	// acquire stock
	public Completes<Response> handleAcquireStock(String id, AcquireStockData data) {
		return resolve(id).andThenTo(portfolio -> portfolio.stockAcquired(data.symbol, data.amount))
				.andThenTo(state -> Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")),
						serialized(PortfolioData.from(state)))))
				.otherwise(noGreeting -> Response.of(NotFound, location(id)));
	}

	// get all stocks
	public Completes<Response> handleAllStocks() {
		return Completes.withSuccess(
				Response.of(Ok, headers(of(ContentType, "application/json")), serialized(QuotesCache.INSTANCE.get())));
	}

	@Override
	public Resource<?> routes() {
		return resource("Portfolio Resource", get("/portfolio/{id}").param(String.class).handle(this::handleGet),
				post("/portfolio").body(String.class).handle(this::handlePost), post("/portfolio/{id}")
						.param(String.class).body(AcquireStockData.class).handle(this::handleAcquireStock),
				get("/stocks").handle(this::handleAllStocks));
	}

	private String location(final String id) {
		return "/portfolio/" + id;
	}

	private Completes<Portfolio> resolve(final String id) {
		return stage.actorOf(Portfolio.class, stage.addressFactory().from(id), PortfolioEntity.class);
	}

}
