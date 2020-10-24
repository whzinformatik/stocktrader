package com.whz.feedback.infrastructure.persistence;

import com.whz.feedback.infrastructure.FeedbackData;
import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;
import java.util.ArrayList;
import java.util.Collection;

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
