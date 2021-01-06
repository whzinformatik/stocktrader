/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <lationts@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.model.account;

public final class AccountState {
  public String id;
  public double balance;
  public double totalInvested;
  public Loyalty loyalty;
  public double commissions;
  public int free;
  public Sentiment sentiment;

  public static AccountState identifiedBy(final String id) {
    return new AccountState(id);
  }

  public boolean doesNotExist() {
    return id == null;
  }

  public boolean isIdentifiedOnly() {
    return id != null
        && balance == 0.0
        && totalInvested == 0.0
        && loyalty == null
        && commissions == 0.0
        && free == 0
        && sentiment == null;
  }

  public AccountState withCreationValues(double balance) {
    return new AccountState(id, balance, 0.0);
  }

  public AccountState withInvestedMoney(double amount) {
    return new AccountState(id, 0.0, amount);
  }

  public AccountState(final String id, double balance, double amount) {
    setDefaultValues();

    this.id = id;
    this.balance = balance;
    this.totalInvested = amount;
  }

  /** Sets the initial variables when an Account is created. */
  public void setDefaultValues() {
    this.totalInvested = 0.0;
    this.loyalty = Loyalty.BASIC;
    this.commissions = 8.99;
    this.free = 0;
    this.sentiment = Sentiment.UNKNOWN;
  }

  private AccountState(final String id) {
    this(id, 0.0, 0.0);
  }

  private AccountState() {
    this(null, 0.0, 0.0);
  }
}
