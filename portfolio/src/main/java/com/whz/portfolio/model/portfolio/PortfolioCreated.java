/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.model.portfolio;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

/**
 * Event class. Contains information for the creation of a new Portfolio.
 *
 * @since 1.0.0
 */
public final class PortfolioCreated extends IdentifiedDomainEvent {

  public final String id;
  public final String owner;

  public PortfolioCreated(final String id, final String owner) {
    this.id = id;
    this.owner = owner;
  }

  @Override
  public String identity() {
    return id;
  }
}
