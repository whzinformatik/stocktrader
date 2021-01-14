/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.infrastructure.persistence;

import com.whz.portfolio.infrastructure.PortfolioData;
import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Generated class by 'VLINGO/XOOM Starter'.
 *
 * @since 1.0.0
 */
public class PortfolioQueriesActor extends StateStoreQueryActor implements PortfolioQueries {

  public PortfolioQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<PortfolioData> portfolioOf(String id) {
    return queryStateFor(id, PortfolioData.class, PortfolioData.empty());
  }

  @Override
  public Completes<Collection<PortfolioData>> portfolios() {
    return streamAllOf(PortfolioData.class, new ArrayList<>());
  }
}
