package com.whz.feedback.resource;

import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.resource;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.OutputKeys;

public class FeedbackResource extends ResourceHandler {

	private static final Logger LOG = LoggerFactory.getLogger(FeedbackResource.class);

	private final Stage stage;

	public FeedbackResource(final Stage stage) {
		this.stage = stage;
	}

	public Completes<Response> ready(){
		return Completes.withSuccess(Response.of(Response.Status.Ok));
	}

	@Override
	public Resource<?> routes() {
		LOG.info("calling ready...");
		return resource(getClass().getSimpleName(),
				get("/ready").handle(this::ready));
	}
}