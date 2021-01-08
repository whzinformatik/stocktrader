/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.infrastructure;

/**
 * DTO object which represents an acquired Stock in the Portfolio microservice. The given amount
 * will be added to the Account's totalInvested value.
 *
 * @author Lation
 */
public class StockData {

  public String owner;
  public double amount;

  public StockData(String owner, double amount) {
    this.owner = owner;
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "BoughtStockData [owner=" + owner + ", amount=" + amount + "]";
  }
}
