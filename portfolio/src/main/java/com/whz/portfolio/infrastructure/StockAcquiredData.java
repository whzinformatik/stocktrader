/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.infrastructure;

/**
 * Class for StockAcquiredPublisher. Used to notify the account about a stock purchase.
 *
 * @since 1.0.0
 */
public class StockAcquiredData {

  public String owner;
  public double value;

  public StockAcquiredData(String owner, double value) {
    this.owner = owner;
    this.value = value;
  }
}
