package com.whz.feedback.infrastructure.persistence;

import com.whz.feedback.infrastructure.FeedbackData;
import io.vlingo.common.Completes;

import java.util.Collection;

public interface FeedbackQueries {
	
  Completes<FeedbackData> feedbackOf(String id);
  Completes<Collection<FeedbackData>> feedbacks();
}