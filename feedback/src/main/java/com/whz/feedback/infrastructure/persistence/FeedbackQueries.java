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
import java.util.Collection;

public interface FeedbackQueries {

  Completes<FeedbackData> feedbackOf(String id);

  Completes<Collection<FeedbackData>> feedbacks();
}
