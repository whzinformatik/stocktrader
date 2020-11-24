package com.whz.account.model.account;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class AccountCreated extends IdentifiedDomainEvent {
	public final String id;
	public final String placeholderValue;

	public AccountCreated(final String id, final String placeholderValue) {
		this.id = id;
		this.placeholderValue = placeholderValue;
	}

	@Override
	public String identity() {
		return id;
	}
}
