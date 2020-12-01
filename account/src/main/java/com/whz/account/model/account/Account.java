package com.whz.account.model.account;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Account {

	static Completes<AccountState> defineWith(final Stage stage, final double balance) {
		final Address address = stage.world().addressFactory().uniquePrefixedWith("g-");
		final Account account = stage.actorFor(Account.class,
				Definition.has(AccountEntity.class, Definition.parameters(address.idString())), address);
		return account.accountCreated(balance);
	}

	Completes<AccountState> accountCreated(final double balance);

}