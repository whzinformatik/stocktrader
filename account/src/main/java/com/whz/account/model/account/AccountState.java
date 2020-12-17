/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <lationts@gmail.com>
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
  public String loyalty;
  public double commissions;
  public int free;
  public String sentiment;

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
    return new AccountState(id, balance);
  }

  public AccountState(final String id, double balance) {
    this.id = id;
    this.balance = balance;

    initAccount();
  }

  /** Sets the initial variables when an Account is created. */
  public void initAccount() {
    this.totalInvested = 0.0;
    this.loyalty = "Basic";
    this.commissions = 8.99;
    this.free = 0;
    this.sentiment = "Neutral";
  }

  private AccountState(final String id) {
    this(id, 0.0);
  }

  private AccountState() {
    this(null, 0.0);
  }
}
