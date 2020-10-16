package com.whz.feedback.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import com.whz.feedback.infrastructure.FeedbackData;

public interface FeedbackQueries {
	
  Completes<FeedbackData> feedbackOf(String id);
  Completes<Collection<FeedbackData>> feedbacks();
}