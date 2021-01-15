/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.model.account;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

/**
 * SentimentReceived Event which is responsible for changing the sentiment of an Account based on
 * the given assessment.
 *
 * @since 1.0.0
 */
public class SentimentReceived extends IdentifiedDomainEvent {

  public String id;
  public Sentiment sentiment;

  public SentimentReceived(final String id, Sentiment sentiment) {
    this.id = id;
    this.sentiment = sentiment;
  }

  @Override
  public String identity() {
    return id;
  }
}
