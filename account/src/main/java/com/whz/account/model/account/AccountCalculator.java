/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.model.account;

public class AccountCalculator {

  /**
   * Calculates the new Loyalty based on the given total invested money.
   *
   * @since 1.0.0
   * @param totalInvested - total invested money
   * @return Loyalty - calculated Loyalty
   */
  public static Loyalty calculateLoyalty(double totalInvested) {
    if (totalInvested > 1000000.00) return Loyalty.PLATINUM;
    else if (totalInvested > 100000.00) return Loyalty.GOLD;
    else if (totalInvested > 50000.00) return Loyalty.SILVER;
    else if (totalInvested > 10000.00) return Loyalty.BRONZE;
    else return Loyalty.BASIC;
  }

  /**
   * Calculates an additional free amount that will be added to the account based on the given
   * sentiment.
   *
   * @param sentiment - received sentiment
   * @return int - calculated free amount
   */
  public static int calculateFree(Sentiment sentiment) {
    if (sentiment == Sentiment.POSITIVE) return 3;
    else if (sentiment == Sentiment.NEUTRAL) return 1;
    else if (sentiment == Sentiment.NEGATIVE) return 0;
    else if (sentiment == Sentiment.UNKNOWN) return 0;
    else return 0;
  }
}
