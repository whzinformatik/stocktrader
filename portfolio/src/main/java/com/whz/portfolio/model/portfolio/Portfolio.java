package com.whz.portfolio.model.portfolio;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Portfolio {

  static Completes<PortfolioState> definePlaceholder(final Stage stage, final String placeholderValue) {
    final Address address = stage.world().addressFactory().uniquePrefixedWith("g-");
    final Portfolio portfolio = stage.actorFor(Portfolio.class, Definition.has(PortfolioEntity.class, Definition.parameters(address.idString())), address);
    return portfolio.portfolioOpened(placeholderValue);
  }

  Completes<PortfolioState> portfolioOpened(final String placeholderValue);

}