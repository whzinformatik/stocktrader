/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.model.portfolio;

import java.util.ArrayList;
import java.util.List;

public final class PortfolioState {
  public final String id;
  public final String owner;
  public final List<Stock> stocks;

  public static PortfolioState identifiedBy(final String id) {
    return new PortfolioState(id);
  }

  public boolean doesNotExist() {
    return id == null;
  }

  public boolean isIdentifiedOnly() {
    return id != null && owner == null && stocks == null;
  }

  public PortfolioState withOwner(final String owner) {
    return new PortfolioState(id, owner, new ArrayList<>());
  }

  public PortfolioState withAcquiredStock(final Stock stock) {
    PortfolioState state = new PortfolioState(id, owner, stocks);
    state.stocks.add(stock);
    return state;
  }

  private PortfolioState(final String id, final String owner, final List<Stock> stocks) {
    this.id = id;
    this.owner = owner;
    this.stocks = stocks;
  }

  private PortfolioState(final String id) {
    this(id, null, null);
  }

  private PortfolioState() {
    this(null, null, null);
  }
}
