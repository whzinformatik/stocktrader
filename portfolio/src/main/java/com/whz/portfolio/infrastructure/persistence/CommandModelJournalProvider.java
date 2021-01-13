/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.infrastructure.persistence;

import com.whz.portfolio.model.portfolio.PortfolioCreated;
import com.whz.portfolio.model.portfolio.PortfolioEntity;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.xoom.actors.Settings;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;
import io.vlingo.xoom.storage.Model;
import io.vlingo.xoom.storage.StoreActorBuilder;

/**
 * Generated class by 'VLINGO/XOOM Starter'.
 * 
 * @since 1.0.0
 */
public class CommandModelJournalProvider {
	private static CommandModelJournalProvider instance;

	public final Journal<String> journal;

	public static CommandModelJournalProvider instance() {
		return instance;
	}

	public static CommandModelJournalProvider using(final Stage stage, final SourcedTypeRegistry registry) {
		final Dispatcher noop = new Dispatcher() {
			public void controlWith(final DispatcherControl control) {
			}

			public void dispatch(Dispatchable d) {
			}
		};

		return using(stage, registry, noop);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public static CommandModelJournalProvider using(final Stage stage, final SourcedTypeRegistry registry,
			final Dispatcher dispatcher) {
		if (instance != null) {
			return instance;
		}

		final EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(stage.world());

		entryAdapterProvider.registerAdapter(PortfolioCreated.class, new EventAdapter<>());

		final Journal<String> journal = StoreActorBuilder.from(stage, Model.COMMAND, dispatcher, StorageType.JOURNAL,
				Settings.properties(), true);

		registry.register(new Info(journal, PortfolioEntity.class, PortfolioEntity.class.getSimpleName()));

		instance = new CommandModelJournalProvider(journal);

		return instance;
	}

	private CommandModelJournalProvider(final Journal<String> journal) {
		this.journal = journal;
	}

	public static void reset() {
		instance = null;
	}
}
