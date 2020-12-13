/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.kb1prb13@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.whz.commenttone.model;

public final class CommentTone {
  public final String id;
  public final String message;
  public String sentiment;

  public CommentTone(String id, String message, String sentiment) {
    this.id = id;
    this.message = message;
    this.sentiment = sentiment;
  }

  public void setSentiment(String sentiment) {
    this.sentiment = sentiment;
  }

  @Override
  public String toString() {
    return "CommentTone{"
        + "id='"
        + id
        + '\''
        + ", message='"
        + message
        + '\''
        + ", sentiment='"
        + sentiment
        + '\''
        + '}';
  }
}
