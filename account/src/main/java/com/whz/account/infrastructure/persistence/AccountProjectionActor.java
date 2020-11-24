package com.whz.account.infrastructure.persistence;

import com.whz.account.infrastructure.AccountData;
import com.whz.account.infrastructure.EventTypes;
import com.whz.account.model.account.AccountCreated;

import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

public class AccountProjectionActor extends StateStoreProjectionActor<AccountData> {
	private static final AccountData Empty = AccountData.empty();

	public AccountProjectionActor() {
		super(QueryModelStateStoreProvider.instance().store);
	}

	@Override
	protected AccountData currentDataFor(final Projectable projectable) {
		return Empty;
	}

	@Override
	protected AccountData merge(final AccountData previousData, final int previousVersion,
			final AccountData currentData, final int currentVersion) {

		AccountData merged = null;

		for (final Source<?> event : sources()) {
			switch (EventTypes.valueOf(event.typeName())) {
			case AccountCreated:
				final AccountCreated accountCreated = typed(event);
				merged = AccountData.from(accountCreated.id, accountCreated.placeholderValue); // TODO: implement actual
																								// merge and appropriate
																								// attributes
				break;
			default:
				merged = Empty;
				logger().warn("Event of type " + event.typeName() + " was not matched.");
				break;
			}
		}

		return merged;
	}

}
