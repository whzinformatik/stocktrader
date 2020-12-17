/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.kb1prb13@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.whz.commenttone.model;

/**
 * This class represents a DTO for a comment message
 *
 * @since 1.0.0
 */
public final class CommentTone {

  /**
   * identifier of the comment
   *
   * @since 1.0.0
   */
  public final String id;

  /**
   * content of the comment
   *
   * @since 1.0.0
   */
  public final String message;

  /**
   * assessment of the comment
   *
   * @since 1.0.0
   */
  public String sentiment;

  /**
   * Create a new comment DTO.
   *
   * @param id identifier of the comment
   * @param message content of the comment
   * @param sentiment assessment of the comment
   *
   * @since 1.0.0
   */
  public CommentTone(String id, String message, String sentiment) {
    this.id = id;
    this.message = message;
    this.sentiment = sentiment;
  }

  /**
   * set the assessment for comment
    * @param sentiment assessment of the comment
   *
   * @since 1.0.0
   */
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
