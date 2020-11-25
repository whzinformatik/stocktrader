package com.whz.feedback.infrastructure;

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

import com.whz.feedback.model.feedback.FeedbackState;
import java.util.List;
import java.util.stream.Collectors;

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
    return states.stream().map(FeedbackData::from).collect(Collectors.toList());
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
