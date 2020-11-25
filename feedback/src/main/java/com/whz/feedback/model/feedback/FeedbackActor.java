package com.whz.feedback.model.feedback;

/*-
 * #%L
 * feedback
 * %%
 * Copyright (C) 2020 Fachgruppe Informatik WHZ
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import com.whz.feedback.resource.Publisher;
import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FeedbackActor extends EventSourced implements Feedback {

  private final Logger logger = LoggerFactory.getLogger(FeedbackActor.class);

  private static final String EXCHANGE_NAME = "feedback";

  private final Publisher<FeedbackState> publisher;

  private FeedbackState state;

  public FeedbackActor(final String id) {
    super(id);
    this.state = FeedbackState.identifiedBy(id);

    final String serviceName =
        Optional.ofNullable(System.getenv("RABBITMQ_SERVICE")).orElse("localhost");
    this.publisher = new Publisher<>(serviceName);
  }

  @Override
  public Completes<FeedbackState> defineWith(final String message) {
    return apply(new FeedbackSubmitted(state.id, message), () -> state);
  }

  // =====================================
  // EventSourced
  // =====================================

  static {
    EventSourced.registerConsumer(
        FeedbackActor.class, FeedbackSubmitted.class, FeedbackActor::applyFeedbackMessage);
  }

  private void applyFeedbackMessage(final FeedbackSubmitted e) {
    state = state.withMessage(e.message);

    try {
      publisher.send(EXCHANGE_NAME, state);
    } catch (Exception ex) {
      logger.error("cannot publish message", ex);
    }
  }
}
