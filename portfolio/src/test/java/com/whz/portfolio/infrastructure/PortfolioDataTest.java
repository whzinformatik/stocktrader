/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import com.whz.portfolio.model.portfolio.PortfolioState;
import org.junit.jupiter.api.Test;

public class PortfolioDataTest {

  private final String id = "new_id";

  private final String owner = "TestOwner";

  @Test
  public void testEmpty() {
    PortfolioData portfolioData = PortfolioData.empty();
    assertTrue(portfolioData.id.isEmpty());
    assertTrue(portfolioData.owner.isEmpty());
  }

  @Test
  public void testFromStateWithNull() {
    PortfolioState portfolioState = PortfolioState.identifiedBy(null);
    PortfolioData portfolioData = PortfolioData.from(portfolioState);
    assertNull(portfolioData.id);
    assertNull(portfolioData.owner);
  }

  @Test
  public void testFromStateNonNull() {
    PortfolioState portfolioState = PortfolioState.identifiedBy(id).withOwner(owner);
    PortfolioData portfolioData = PortfolioData.from(portfolioState);
    assertEquals(id, portfolioData.id);
    assertEquals(owner, portfolioData.owner);
  }
}
