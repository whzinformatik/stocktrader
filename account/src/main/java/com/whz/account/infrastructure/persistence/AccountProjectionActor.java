/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.infrastructure.persistence;

import com.whz.account.infrastructure.AccountData;
import com.whz.account.infrastructure.EventTypes;
import com.whz.account.model.account.AccountCreated;
import com.whz.account.model.account.Loyalty;
import com.whz.account.model.account.MoneyInvested;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

public class AccountProjectionActor extends StateStoreProjectionActor<AccountData> {
  private static final AccountData Empty = AccountData.empty();

  public AccountProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
  }

  @Override
  protected AccountData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  /**
   * Based on the given Event Type and given values the appropriate case will be triggered and the
   * Account object will be created or altered.
   */
  @Override
  protected AccountData merge(
      final AccountData previousData,
      final int previousVersion,
      final AccountData currentData,
      final int currentVersion) {
    if (previousVersion == currentVersion) {
      return previousData;
    }

    for (final Source<?> event : sources()) {
      switch (EventTypes.valueOf(event.typeName())) {
        case AccountCreated:
          final AccountCreated accountCreated = typed(event);
          currentData.id = accountCreated.id;
          currentData.balance = accountCreated.balance;
          currentData.totalInvested = accountCreated.totalInvested;
          currentData.loyalty = accountCreated.loyalty;
          currentData.commissions = accountCreated.commissions;
          currentData.free = accountCreated.free;
          currentData.sentiment = accountCreated.sentiment;
          break;
        case MoneyInvested:
          final MoneyInvested moneyInvested = typed(event);
          currentData.id = previousData.id;
          currentData.totalInvested += moneyInvested.amount;

          if (currentData.totalInvested > 1000000.00) {
            currentData.loyalty = Loyalty.PLATINUM;
          } else if (currentData.totalInvested > 100000.00) {
            currentData.loyalty = Loyalty.GOLD;
          } else if (currentData.totalInvested > 50000.00) {
            currentData.loyalty = Loyalty.SILVER;
          } else if (currentData.totalInvested > 10000.00) {
            currentData.loyalty = Loyalty.BRONZE;
          } else {
            currentData.loyalty = Loyalty.BASIC;
          }
          break;
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return currentData;
  }
}
