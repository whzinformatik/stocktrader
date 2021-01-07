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
 * AccountCreated Event which is responsible for creating a fresh account based on a given initial
 * balance. It contains an id, an initial balance, the amount of totalInvested money, the loyalty
 * and sentiment of the account, the current commissions for buying stocks and a free integer which
 * represents how many stocks can be bought for free.
 *
 * @author Lation
 */
public final class AccountCreated extends IdentifiedDomainEvent {
  public String id;
  public double balance;
  public double totalInvested;
  public Loyalty loyalty;
  public double commissions;
  public int free;
  public Sentiment sentiment;

  /**
   * AccountCreated Event constructor.
   *
   * @param id - id of the corresponding account
   * @param balance - initial balance of the account
   */
  public AccountCreated(final String id, double balance) {
    this.id = id;
    this.balance = balance;

    initAccount();
  }

  /** Sets the initial values when an Account is created. */
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
