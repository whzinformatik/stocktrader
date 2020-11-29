package com.whz.portfolio.infrastructure;

import com.whz.portfolio.model.portfolio.PortfolioState;
import com.whz.portfolio.model.portfolio.Stock;

import java.util.ArrayList;
import java.util.List;

public class PortfolioData {
	// vlingo
  public String id;
  
  public String owner;
  public List<Stock> stockList = new ArrayList<>();
  public double total; 

  public static PortfolioData empty() {
    return new PortfolioData("", "");
  }

  public static PortfolioData from(final PortfolioState state) {
    return new PortfolioData(state.id, state.owner);
  }

  public static List<PortfolioData> from(final List<PortfolioState> states) {
    final List<PortfolioData> data = new ArrayList<>(states.size());

    for (final PortfolioState state : states) {
      data.add(PortfolioData.from(state));
    }

    return data;
  }

  public static PortfolioData from(final String id, final String owner) {
    return new PortfolioData(id, owner);
  }

  public static PortfolioData just(final String owner) {
    return new PortfolioData("", owner);
  }

  @Override
  public String toString() {
    return "PortfolioData [id=" + id + " owner=" + owner + "]";
  }

  private PortfolioData(final String id, final String owner) {
    this.id = id;
    this.owner = owner;
  }
}
