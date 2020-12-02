package com.whz.commenttone.resource;

import com.whz.commenttone.infrastructure.CommentToneData;
import com.whz.commenttone.infrastructure.persistence.CommentToneQueries;
import com.whz.commenttone.infrastructure.persistence.QueryModelStateStoreProvider;
import com.whz.commenttone.model.commenttone.CommentTone;
import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;
import static io.vlingo.http.resource.ResourceBuilder.*;


public class CommentToneResource extends ResourceHandler {

    private final Stage stage;
    private final CommentToneQueries commentToneQueries;

    private final Logger logger;

    public CommentToneResource(final Stage stage) {
        this.stage = stage;
        this.commentToneQueries = QueryModelStateStoreProvider.instance().commentToneQueries;
        this.logger = stage.world().defaultLogger();
    }

    // READY
    private Completes<Response> handleReady() {
        return Completes.withSuccess(Response.of(Ok));
    }

    // POST
    public Completes<Response> handlePost(CommentToneData data) {
        return CommentTone.defineWith(stage, data.message, data.sentiment)
                .andThenTo(
                        state ->
                                Completes.withSuccess(
                                        Response.of(
                                                Created,
                                                headers(of(Location, location(state.id)))
                                                        .and(of(ContentType, "application/json")),
                                                serialized(CommentToneData.from(state)))));
    }

    // GET
    public Completes<Response> handleGet(String id) {
        return commentToneQueries.commentToneOf(id)
                .andThenTo(CommentToneData.empty(),
                        commentToneData ->
                                Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json"))))
                                .otherwise(none -> Response.of(NotFound, location(id))));
    }

    //GET
    public Completes<Response> handleGets() {
        return commentToneQueries.commentTones()
                .andThenTo(commentToneData ->
                        Completes.withSuccess(Response.of(Ok, headers(of(ContentType, "application/json")),
                                serialized(commentToneData))));
    }

    @Override
    public Resource<?> routes() {
        logger.info("calling ready...");

        return resource(this.getClass().getSimpleName()
                , get("/ready").handle(this::handleReady)
                , post("/").body(CommentToneData.class).handle(this::handlePost)
                , get("/{id}").param(String.class).handle(this::handleGet)
                , get("/").handle(this::handleGets)
        );
    }

    private String location(final String id) {
        return "/" + id;
    }

}
