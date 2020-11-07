package com.whz.account.model.account;

import io.vlingo.lattice.model.DomainEvent;

public final class AccountCreated extends DomainEvent {
	public final String id;
	public final String placeholderValue;

	public AccountCreated(final String id, final String placeholderValue) {
		this.id = id;
		this.placeholderValue = placeholderValue;
	}
}
