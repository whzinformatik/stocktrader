/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.model.portfolio;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PortfolioStateTest {

  private final String id = "new_id";

  private final String owner = "TestOwner";

  @Test
  public void testNullId() {
    PortfolioState portfolioState = PortfolioState.identifiedBy(null);
    assertNull(portfolioState.id);
    assertNull(portfolioState.owner);
    assertTrue(portfolioState.doesNotExist());
    assertFalse(portfolioState.isIdentifiedOnly());
  }

  @Test
  public void testOnlyId() {
    PortfolioState portfolioState = PortfolioState.identifiedBy(id);
    assertEquals(id, portfolioState.id);
    assertNull(portfolioState.owner);
    assertFalse(portfolioState.doesNotExist());
    assertTrue(portfolioState.isIdentifiedOnly());
  }

  @Test
  public void testIdAndMessage() {
    PortfolioState portfolioState = PortfolioState.identifiedBy(id).withOwner(owner);
    assertEquals(id, portfolioState.id);
    assertEquals(owner, portfolioState.owner);
    assertFalse(portfolioState.doesNotExist());
    assertFalse(portfolioState.isIdentifiedOnly());
  }
}
