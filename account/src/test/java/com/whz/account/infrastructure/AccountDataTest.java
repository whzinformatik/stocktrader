/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.whz.account.model.account.AccountState;
import com.whz.account.model.account.Loyalty;
import com.whz.account.model.account.Sentiment;
import org.junit.jupiter.api.Test;

public class AccountDataTest {

  private final String id = "new_id";

  private final double balance = 40000.0;
  private final double amount = 12000.0;
  private final Sentiment sentiment = Sentiment.POSITIVE;

  @Test
  public void testEmpty() {
    AccountData accountData = AccountData.empty();
    assertTrue(accountData.id.isEmpty());
    assertTrue(accountData.balance == 0);
    assertTrue(accountData.totalInvested == 0);
    assertTrue(accountData.loyalty == Loyalty.BASIC);
    assertTrue(accountData.commissions == 0);
    assertTrue(accountData.free == 0);
    assertTrue(accountData.sentiment == Sentiment.UNKNOWN);
  }

  @Test
  public void testFromStateWithNull() {
    AccountState accountState = AccountState.identifiedBy(null);
    AccountData accountData = AccountData.from(accountState);
    assertNull(accountData.id);
    assertTrue(accountData.balance == 0);
    assertTrue(accountData.totalInvested == 0);
    assertTrue(accountData.loyalty == Loyalty.BASIC);
    assertTrue(accountData.commissions == 8.99);
    assertTrue(accountData.free == 0);
    assertTrue(accountData.sentiment == Sentiment.UNKNOWN);
  }

  @Test
  public void testFromStateWithCreationValues() {
    AccountState accountState = AccountState.identifiedBy(id).withCreationValues(balance);
    AccountData accountData = AccountData.from(accountState);
    assertEquals(id, accountData.id);
    assertEquals(balance, accountData.balance);
  }

  @Test
  public void testFromStateWithInvestedMoney() {
    AccountState accountState = AccountState.identifiedBy(id).withInvestedMoney(amount);
    AccountData accountData = AccountData.from(accountState);
    assertEquals(id, accountData.id);
    assertEquals(amount, accountData.totalInvested);
  }

  @Test
  public void testFromStateWithReceivedSentiment() {
    AccountState accountState = AccountState.identifiedBy(id).withReceivedSentiment(sentiment);
    AccountData accountData = AccountData.from(accountState);
    assertEquals(id, accountData.id);
    assertEquals(Sentiment.POSITIVE, accountData.sentiment);
  }
}
