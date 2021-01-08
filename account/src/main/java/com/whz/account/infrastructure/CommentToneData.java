/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.infrastructure;

import com.whz.account.model.account.Sentiment;

/**
 * DTO object which represents a CommentTone in the CommentTone microservice. The given assessment
 * will be adjusted to the Account's Sentiment.
 *
 * @author Lation
 */
public final class CommentToneData {

  public final String owner;
  public Sentiment sentiment;

  public CommentToneData(String owner, Sentiment sentiment) {
    this.owner = owner;
    this.sentiment = sentiment;
  }

  @Override
  public String toString() {
    return "CommentTone{" + "owner='" + owner + '\'' + ", sentiment='" + sentiment + '\'' + '}';
  }
}
