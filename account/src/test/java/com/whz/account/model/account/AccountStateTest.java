/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.model.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AccountStateTest {

  private final String id = "new_id";

  private final double balance = 40000.0;

  @Test
  public void testNullId() {
    AccountState accountState = AccountState.identifiedBy(null);
    assertNull(accountState.id);

    assertEquals(0, accountState.balance);
    assertEquals(0, accountState.totalInvested);
    assertEquals(Loyalty.BASIC, accountState.loyalty);
    assertEquals(8.99, accountState.commissions);
    assertEquals(0, accountState.free);
    assertEquals(Sentiment.UNKNOWN, accountState.sentiment);

    assertTrue(accountState.doesNotExist());
    assertFalse(accountState.isIdentifiedOnly());
  }

  @Test
  public void testOnlyId() {
    AccountState accountState = AccountState.identifiedBy(id);
    assertEquals(id, accountState.id);
    assertEquals(0, accountState.balance);
    assertFalse(accountState.doesNotExist());
    assertFalse(accountState.isIdentifiedOnly());
  }

  @Test
  public void testIdAndBalance() {
    AccountState accountState = AccountState.identifiedBy(id).withCreationValues(balance);
    assertEquals(id, accountState.id);
    assertEquals(balance, accountState.balance);
    assertFalse(accountState.doesNotExist());
    assertFalse(accountState.isIdentifiedOnly());
  }
}
