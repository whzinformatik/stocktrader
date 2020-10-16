package com.whz.feedback.model.feedback;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

public final class FeedbackActor extends EventSourced implements Feedback {

	private FeedbackState state;

	public FeedbackActor(final String id) {
		super(id);
		this.state = FeedbackState.identifiedBy(id);
	}

	@Override
	public Completes<FeedbackState> defineWith(final String message) {
		return apply(new FeedbackSubmittedEvent(state.id, message), () -> state);
	}

	// =====================================
	// EventSourced
	// =====================================

	static {
		EventSourced.registerConsumer(FeedbackActor.class, FeedbackSubmittedEvent.class,
				FeedbackActor::applyFeedbackMessage);
	}

	private void applyFeedbackMessage(final FeedbackSubmittedEvent e) {
		state = state.withMessage(e.message);
	}
}
