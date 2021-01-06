/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <lationts@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.model.account;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

public class MoneyInvested extends IdentifiedDomainEvent {

  public String id;
  public double amount;

  public MoneyInvested(final String id, double amount) {
    this.id = id;
    this.amount = amount;
  }

  @Override
  public String identity() {
    return id;
  }
}
