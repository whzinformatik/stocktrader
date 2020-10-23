package com.whz.feedback.model.feedback;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class FeedbackStateTest {

    private final String id = "new_id";

    private final String message = "important notice";

    @Test
    public void testNullId(){
        FeedbackState feedbackState = FeedbackState.identifiedBy(null);
        assertNull(feedbackState.id);
        assertNull(feedbackState.message);
        assertTrue(feedbackState.doesNotExist());
        assertFalse(feedbackState.isIdentifiedOnly());
    }

    @Test
    public void testOnlyId(){
        FeedbackState feedbackState = FeedbackState.identifiedBy(id);
        assertEquals(id, feedbackState.id);
        assertNull(feedbackState.message);
        assertFalse(feedbackState.doesNotExist());
        assertTrue(feedbackState.isIdentifiedOnly());
    }

    @Test
    public void testIdAndMessage(){
        FeedbackState feedbackState = FeedbackState.identifiedBy(id).withMessage(message);
        assertEquals(id, feedbackState.id);
        assertEquals(message, feedbackState.message);
        assertFalse(feedbackState.doesNotExist());
        assertFalse(feedbackState.isIdentifiedOnly());
    }
}
