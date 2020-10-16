package com.whz.feedback.infrastructure;

import java.util.ArrayList;
import java.util.List;
import com.whz.feedback.model.feedback.FeedbackState;

public class FeedbackData {
	
  public final String id;
  public final String message;

  public static FeedbackData empty() {
    return new FeedbackData("", "");
  }

  public static FeedbackData from(final FeedbackState state) {
    return new FeedbackData(state.id, state.message);
  }

  public static List<FeedbackData> from(final List<FeedbackState> states) {
    final List<FeedbackData> data = new ArrayList<>(states.size());

    for (final FeedbackState state : states) {
      data.add(FeedbackData.from(state));
    }

    return data;
  }

  public static FeedbackData from(final String id, final String message) {
    return new FeedbackData(id, message);
  }

  public static FeedbackData just(final String message) {
    return new FeedbackData("", message);
  }

  private FeedbackData(final String id, final String message) {
    this.id = id;
    this.message = message;
  }
  
  @Override
  public String toString() {
    return "FeedbackData [id=" + id + " message=" + message + "]";
  }
}
