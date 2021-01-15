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
 * @since 1.0.0
 */
public class StockData {

  public String owner;
  public double value;

  public StockData(String owner, double value) {
    this.owner = owner;
    this.value = value;
  }

  @Override
  public String toString() {
    return "BoughtStockData [owner=" + owner + ", value=" + value + "]";
  }
}
