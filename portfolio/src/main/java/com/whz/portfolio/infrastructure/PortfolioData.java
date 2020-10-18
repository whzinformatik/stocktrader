package com.whz.portfolio.infrastructure;

import java.util.ArrayList;
import java.util.List;
import com.whz.portfolio.model.portfolio.PortfolioState;

public class PortfolioData {
  public final String id;
  public final String placeholderValue;
  
  // TODO
  private String owner;
  private double total;
  private String loyalty;
  private double balance;
  private double commissions;
  private int free;
  private String sentiment;
  private double nextCommission;

 // private List<Stock> stockList = new ArrayList<Stock>();

  public static PortfolioData empty() {
    return new PortfolioData("", "");
  }

  public static PortfolioData from(final PortfolioState state) {
    return new PortfolioData(state.id, state.placeholderValue);
  }

  public static List<PortfolioData> from(final List<PortfolioState> states) {
    final List<PortfolioData> data = new ArrayList<>(states.size());

    for (final PortfolioState state : states) {
      data.add(PortfolioData.from(state));
    }

    return data;
  }

  public static PortfolioData from(final String id, final String placeholderValue) {
    return new PortfolioData(id, placeholderValue);
  }

  public static PortfolioData just(final String placeholderValue) {
    return new PortfolioData("", placeholderValue);
  }

  @Override
  public String toString() {
    return "PortfolioData [id=" + id + " placeholderValue=" + placeholderValue + "]";
  }

  private PortfolioData(final String id, final String placeholderValue) {
    this.id = id;
    this.placeholderValue = placeholderValue;
  }
}
