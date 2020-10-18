package com.whz.portfolio.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import com.whz.portfolio.infrastructure.PortfolioData;

public interface PortfolioQueries {
  Completes<PortfolioData> portfolioOf(String id);
  Completes<Collection<PortfolioData>> portfolios();
}