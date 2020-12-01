package com.whz.account.model.account;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class AccountCreated extends IdentifiedDomainEvent {
  public String id;
  public double balance;
  public double totalInvested; // sinnvoller als an "total" festzumachen
  public String loyalty;
  public double commissions;
  public int free;
  public String sentiment;

  // if (totalInvested > 1000000.00) {
  // loyalty = "Platinum";
  // } else if (totalInvested > 100000.00) {
  // loyalty = "Gold";
  // } else if (totalInvested > 50000.00) {
  // loyalty = "Silver";
  // } else if (totalInvested > 10000.00) {
  // loyalty = "Bronze";
  // } else {
  // loyalty = "Basic";
  // }

  public AccountCreated(final String id, double balance) {
    this.id = id;
    this.balance = balance;

    initAccount();
  }

  /** Sets the initial variables when an Account is created. */
  public void initAccount() {
    this.totalInvested = 0.0;
    this.loyalty = "Basic";
    this.commissions = 8.99;
    this.free = 0;
    this.sentiment = "Neutral";
  }

  @Override
  public String identity() {
    return id;
  }
}
