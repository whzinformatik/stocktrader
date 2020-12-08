/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.infrastructure.persistence;

import com.whz.feedback.infrastructure.FeedbackData;
import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class represents an actor that queries asynchronously for a state of a feedback message by
 * id.
 *
 * @since 1.0.0
 */
public class FeedbackQueriesActor extends StateStoreQueryActor implements FeedbackQueries {

  public FeedbackQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<FeedbackData> feedbackOf(String id) {
    return queryStateFor(id, FeedbackData.class, FeedbackData.empty());
  }

  @Override
  public Completes<Collection<FeedbackData>> feedbacks() {
    return streamAllOf(FeedbackData.class, new ArrayList<>());
  }
}
