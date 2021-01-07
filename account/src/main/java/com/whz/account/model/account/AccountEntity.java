/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <lationts@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.model.account;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

/**
 * AccountEntity managing the Account object by implementing it and realizing
 * the necessary methods through appropriate calls.
 * 
 * @author Lation
 */
public final class AccountEntity extends EventSourced implements Account {
	private AccountState state;

	public AccountEntity(final String id) {
		super(id);
		this.state = AccountState.identifiedBy(id);
	}

	@Override
	public Completes<AccountState> accountCreated(final double balance) {
		return apply(new AccountCreated(state.id, balance), () -> state);
	}

	@Override
	public Completes<AccountState> moneyInvested(final double amount) {
		return apply(new MoneyInvested(state.id, amount), () -> state);
	}

	// =====================================
	// EventSourced
	// =====================================

	static {
		EventSourced.registerConsumer(AccountEntity.class, AccountCreated.class, AccountEntity::applyAccountCreated);
		EventSourced.registerConsumer(AccountEntity.class, MoneyInvested.class, AccountEntity::applyMoneyInvested);
	}

	/**
	 * Calls a method to create an Account via an initial balance.
	 * 
	 * @param e - AccountCreated Event
	 */
	private void applyAccountCreated(final AccountCreated e) {
		state = state.withCreationValues(e.balance);
	}

	/**
	 * Calls a method to add invested money to an Account.
	 * 
	 * @param e - MoneyInvested Event
	 */
	private void applyMoneyInvested(final MoneyInvested e) {
		state = state.withInvestedMoney(e.amount);
	}
}
