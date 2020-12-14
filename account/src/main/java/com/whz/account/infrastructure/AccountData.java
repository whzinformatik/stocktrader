/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <lationts@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.infrastructure;

import com.whz.account.model.account.AccountState;
import java.util.ArrayList;
import java.util.List;

public class AccountData {
  public String id;
  public double balance;
  public double totalInvested;
  public String loyalty;
  public double commissions;
  public int free;
  public String sentiment;

  public static AccountData empty() {
    return new AccountData("", 0.0, 0.0, "", 0.0, 0, "");
  }

  public static AccountData from(final AccountState state) {
    return new AccountData(
        state.id,
        state.balance,
        state.totalInvested,
        state.loyalty,
        state.commissions,
        state.free,
        state.sentiment);
  }

  public static List<AccountData> from(final List<AccountState> states) {
    final List<AccountData> data = new ArrayList<>(states.size());

    for (final AccountState state : states) {
      data.add(AccountData.from(state));
    }

    return data;
  }

  public static AccountData from(
      final String id,
      double balance,
      double totalInvested,
      String loyalty,
      double commissions,
      int free,
      String sentiment) {
    return new AccountData(id, balance, totalInvested, loyalty, commissions, free, sentiment);
  }

  public static AccountData just(double balance) {
    return new AccountData("", balance, 0.0, "", 0.0, 0, "");
  }

  @Override
  public String toString() {
    return "AccountData [id="
        + id
        + ", totalInvested="
        + totalInvested
        + ", loyalty="
        + loyalty
        + ", balance="
        + balance
        + ", commissions="
        + commissions
        + ", free="
        + free
        + ", sentiment="
        + sentiment
        + "]";
  }

  public AccountData(
      final String id,
      double balance,
      double totalInvested,
      String loyalty,
      double commissions,
      int free,
      String sentiment) {
    this.id = id;
    this.balance = balance;
    this.totalInvested = totalInvested;
    this.loyalty = loyalty;
    this.commissions = commissions;
    this.free = free;
    this.sentiment = sentiment;
  }
}
