package com.whz.account.model.account;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Account {

  static Completes<AccountState> definePlaceholder(final Stage stage, final String placeholderValue) {
    final Address address = stage.world().addressFactory().uniquePrefixedWith("g-");
    final Account account = stage.actorFor(Account.class, Definition.has(AccountEntity.class, Definition.parameters(address.idString())), address);
    return account.definePlaceholder(placeholderValue);
  }

  Completes<AccountState> definePlaceholder(final String placeholderValue);

}