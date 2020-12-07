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
import com.whz.portfolio.infrastructure.StockQuoteData;
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
		// access attempt to initialize the singletons
		StockQuoteSubscriber sub = StockQuoteSubscriber.INSTANCE;
		StockAcquiredPublisher pub = StockAcquiredPublisher.INSTANCE;
		
		this.stage = stage;
		portfolioQueries = QueryModelStateStoreProvider.instance().portfolioQueries;
	}

	/**
	 * Retrieve an exiting portfolio.
	 * 
	 * @param id
	 * @return A JSON representation of PortfolioData
	 */
	public Completes<Response> handleGetPortfolio(String id) {
		return portfolioQueries.portfolioOf(id)
				.andThenTo(PortfolioData.empty(),
						data -> Completes.withSuccess(
								Response.of(Ok, headers(of(ContentType, "application/json")), serialized(data))))
				.otherwise(noData -> Response.of(NotFound, "/portfolio/" + id));
	}

	/**
	 * Create a new portfolio.
	 * 
	 * @param owner
	 * @return JSON representation of the created PortfolioData
	 */
	public Completes<Response> handleCreatePortfolio(String owner) {
		return Portfolio.defineWith(stage, owner)
				.andThenTo(state -> Completes.withSuccess(Response.of(Created,
						headers(of(Location, "/portfolio/" + state.id)).and(of(ContentType, "application/json")),
						serialized(PortfolioData.from(state)))));
	}

	/**
	 * Add a stock to an existing portfolio.
	 * 
	 * @param id
	 * @param data
	 * @return JSON representation of the associated PortfolioData
	 */
	public Completes<Response> handleAcquireStock(String id, AcquireStockData data) {
		return resolve(id).andThenTo(portfolio -> {
			StockQuoteData stockQuoteData = QuotesCache.INSTANCE.get(data.symbol);
			return portfolio.stockAcquired(data.symbol, stockQuoteData.regularMarketTime, data.amount,
					stockQuoteData.regularMarketPrice);
		}).andThenTo(state -> Completes.withSuccess(
				Response.of(Ok, headers(of(ContentType, "application/json")), serialized(PortfolioData.from(state)))))
				.otherwise(noData -> Response.of(NotFound, "/portfolio/" + id));
	}

	/**
	 * Retrieve a specific stock.
	 * 
	 * @param symbol
	 * @param time
	 * @return JSON representation of StockQuoteData
	 */
	public Completes<Response> handleGetStock(String symbol, long time) {
		StockQuoteData result = StockQuoteSubscriber.INSTANCE.get(symbol, time);
		if (result == null) {
			Response.of(NotFound, "/stocks/" + symbol);
		}
		return Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")), serialized(result)));
	}

	/**
	 * Retrieve all stocks from the cache
	 * 
	 * @return JSON representation of multiple StockQuoteData
	 */
	public Completes<Response> handleGetStocks() {
		return Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")),
				serialized(StockQuoteSubscriber.INSTANCE.get())));
	}

	@Override
	public Resource<?> routes() {
		return resource("Portfolio Resource",
				get("/portfolio/{id}").param(String.class).handle(this::handleGetPortfolio),
				post("/portfolio").body(String.class).handle(this::handleCreatePortfolio),
				post("/portfolio/{id}").param(String.class).body(AcquireStockData.class)
						.handle(this::handleAcquireStock),
				get("/stocks/{symbol}/{time}").param(String.class).param(Long.class).handle(this::handleGetStock),
				get("/stocks").handle(this::handleGetStocks));

	}

	private Completes<Portfolio> resolve(final String id) {
		return stage.actorOf(Portfolio.class, stage.addressFactory().from(id), PortfolioEntity.class);
	}

}
