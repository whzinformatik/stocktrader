package com.whz.portfolio.resource;

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
    QuotesCache test = QuotesCache.INSTANCE;
    this.stage = stage;
    this.portfolioQueries = QueryModelStateStoreProvider.instance().portfolioQueries;
  }

  // GET
  public Completes<Response> handleGet(String id) {
    return portfolioQueries
        .portfolioOf(id)
        .andThenTo(
            PortfolioData.empty(),
            data ->
                Completes.withSuccess(
                    Response.of(
                        Ok, headers(of(ContentType, "application/json")), serialized(data))))
        .otherwise(noData -> Response.of(NotFound, location(id)));
  }

  // POST
  public Completes<Response> handlePost(PortfolioData data) {
    return Portfolio.defineWith(stage, data.owner)
        .andThenTo(
            state ->
                Completes.withSuccess(
                    Response.of(
                        Created,
                        headers(of(Location, location(state.id)))
                            .and(of(ContentType, "application/json")),
                        serialized(PortfolioData.from(state)))));
  }

  // PUT
  public Completes<Response> handlePut(PortfolioData data) {
    return Completes.withSuccess(
        Response.of(Ok, headers(of(Location, location(data.id))), "Updated"));
  }

  // DELETE
  public Completes<Response> handleDelete(String id) {
    return Completes.withSuccess(Response.of(Ok, "Deleted"));
  }

  // STOCKS ALL
  public Completes<Response> handleAllStocks() {
    return Completes.withSuccess(
        Response.of(
            Ok,
            headers(of(ContentType, "application/json")),
            serialized(QuotesCache.INSTANCE.get())));
  }

  // STOCKS PORTFOLIO
  public Completes<Response> handleStocks(String id) {
    return null;
  }

  @Override
  public Resource<?> routes() {
    return resource(
        getClass().getSimpleName(),
        get("/portfolio/{id}").param(String.class).handle(this::handleGet),
        post("/portfolio").body(PortfolioData.class).handle(this::handlePost),
        put("/portfolio").body(PortfolioData.class).handle(this::handlePut),
        delete("/portfolio/{id}").param(String.class).handle(this::handleDelete),
        get("/stocks").handle(this::handleAllStocks),
        get("/stocks/{id}").param(String.class).handle(this::handleStocks));
  }

  private String location(final String id) {
    return "/portfolio/" + id;
  }

  private Completes<Portfolio> resolve(final String id) {
    return stage.actorOf(Portfolio.class, stage.addressFactory().from(id), PortfolioEntity.class);
  }
}
