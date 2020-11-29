package com.whz.portfolio.infrastructure.persistence;

import com.whz.portfolio.infrastructure.PortfolioData;
import io.vlingo.common.Completes;
import java.util.Collection;

public interface PortfolioQueries {

  Completes<PortfolioData> portfolioOf(String id);

  Completes<Collection<PortfolioData>> portfolios();
  
}
