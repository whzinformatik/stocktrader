/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <lationts@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.model.account;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class AccountCreated extends IdentifiedDomainEvent {
  public String id;
  public double balance;
  public double totalInvested;
  public Loyalty loyalty;
  public double commissions;
  public int free;
  public Sentiment sentiment;

  public AccountCreated(final String id, double balance) {
    this.id = id;
    this.balance = balance;

    initAccount();
  }

  /** Sets the initial variables when an Account is created. */
  public void initAccount() {
    this.totalInvested = 0.0;
    this.loyalty = Loyalty.BASIC;
    this.commissions = 8.99;
    this.free = 0;
    this.sentiment = Sentiment.UNKNOWN;
  }

  @Override
  public String identity() {
    return id;
  }
}
