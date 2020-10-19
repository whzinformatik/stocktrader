package com.whz.account.model.account;

import io.vlingo.lattice.model.DomainEvent;

public final class AccountPlaceholderDefined extends DomainEvent {
  public final String id;
  public final String placeholderValue;

  public AccountPlaceholderDefined(final String id, final String placeholderValue) {
    this.id = id;
    this.placeholderValue = placeholderValue;
  }
}
