package com.whz.feedback.model.feedback;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Feedback {

	static Completes<FeedbackState> defineWith(final Stage stage, final String message) {
		final Address address = stage.world().addressFactory().uniquePrefixedWith("g-");
		final Feedback feedbackActor = stage.actorFor(Feedback.class,
				Definition.has(FeedbackActor.class, Definition.parameters(address.idString())), address);
		return feedbackActor.defineWith(message);
	}

	Completes<FeedbackState> defineWith(final String message);
}