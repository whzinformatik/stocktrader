package com.whz.account.model.account;

public final class AccountState {
  public final String id;
  public final String placeholderValue;

  public static AccountState identifiedBy(final String id) {
    return new AccountState(id);
  }

  public boolean doesNotExist() {
    return id == null;
  }

  public boolean isIdentifiedOnly() {
    return id != null && placeholderValue == null;
  }

  public AccountState withPlaceholderValue(final String value) {
    return new AccountState(this.id, value);
  }

  private AccountState(final String id, final String value) {
    this.id = id;
    this.placeholderValue = value;
  }

  private AccountState(final String id) {
    this(id, null);
  }

  private AccountState() {
    this(null, null);
  }
}
