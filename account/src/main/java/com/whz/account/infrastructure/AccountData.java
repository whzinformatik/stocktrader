package com.whz.account.infrastructure;

import java.util.ArrayList;
import java.util.List;
import com.whz.account.model.account.AccountState;

public class AccountData {
  public final String id;
  public final String placeholderValue;

  public static AccountData empty() {
    return new AccountData("", "");
  }

  public static AccountData from(final AccountState state) {
    return new AccountData(state.id, state.placeholderValue);
  }

  public static List<AccountData> from(final List<AccountState> states) {
    final List<AccountData> data = new ArrayList<>(states.size());

    for (final AccountState state : states) {
      data.add(AccountData.from(state));
    }

    return data;
  }

  public static AccountData from(final String id, final String placeholderValue) {
    return new AccountData(id, placeholderValue);
  }

  public static AccountData just(final String placeholderValue) {
    return new AccountData("", placeholderValue);
  }

  @Override
  public String toString() {
    return "AccountData [id=" + id + " placeholderValue=" + placeholderValue + "]";
  }

  private AccountData(final String id, final String placeholderValue) {
    this.id = id;
    this.placeholderValue = placeholderValue;
  }
}
