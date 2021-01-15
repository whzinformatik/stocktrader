/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.model.account;

/**
 * AccountState managing the Account object. It is responsible for creating and changing an Account
 * object.
 *
 * @since 1.0.0
 */
public final class AccountState {
  public String id;
  public double balance;
  public double totalInvested;
  public Loyalty loyalty;
  public double commissions;
  public int free;
  public Sentiment sentiment;

  public static AccountState identifiedBy(final String id) {
    return new AccountState(id);
  }

  public boolean doesNotExist() {
    return id == null;
  }

  public boolean isIdentifiedOnly() {
    return id != null
        && balance == 0.0
        && totalInvested == 0.0
        && loyalty == null
        && commissions == 0.0
        && free == 0
        && sentiment == null;
  }

  /**
   * Method which will be called when an Account gets created via a given initial balance.
   *
   * @since 1.0.0
   * @param balance - the initial balance
   * @return AccountState - temporary AccountState object containing all necessary information
   */
  public AccountState withCreationValues(double balance) {
    return new AccountState(id, balance, 0.0, Sentiment.UNKNOWN);
  }

  /**
   * Method which will be called when an Account invests money via a Portfolio by buying a stock.
   *
   * @since 1.0.0
   * @param amount - amount of the added invested money
   * @return AccountState - temporary AccountState object containing all necessary information
   */
  public AccountState withInvestedMoney(double amount) {
    return new AccountState(id, 0.0, amount, Sentiment.UNKNOWN);
  }

  /**
   * Method which will be called when an Account receives a sentiment via a CommentTone by giving an
   * assessment.
   *
   * @since 1.0.0
   * @param sentiment - the sentiment which will be changed
   * @return AccountState - temporary AccountState object containing all necessary information
   */
  public AccountState withReceivedSentiment(Sentiment sentiment) {
    return new AccountState(id, 0.0, 0.0, sentiment);
  }

  /**
   * Constructor of an AccountState object which represents an Account.
   *
   * @since 1.0.0
   * @param id - id of the corresponding account
   * @param balance - current balance of the account which will be set when creating an account
   * @param amount - current amount of invested money which will be later added to the account
   */
  public AccountState(final String id, double balance, double amount, Sentiment sentiment) {
    setDefaultValues();

    this.id = id;
    this.balance = balance;
    this.totalInvested = amount;
    this.sentiment = sentiment;
  }

  /**
   * Sets the initial variables when an Account is created.
   *
   * @since 1.0.0
   */
  private void setDefaultValues() {
    this.totalInvested = 0.0;
    this.loyalty = Loyalty.BASIC;
    this.commissions = 8.99;
    this.free = 0;
    this.sentiment = Sentiment.UNKNOWN;
  }

  private AccountState(final String id) {
    this(id, 0.0, 0.0, Sentiment.UNKNOWN);
  }

  private AccountState() {
    this(null, 0.0, 0.0, Sentiment.UNKNOWN);
  }
}
