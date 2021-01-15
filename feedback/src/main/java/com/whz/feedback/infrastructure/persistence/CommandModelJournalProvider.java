/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.infrastructure.persistence;

import com.whz.feedback.model.feedback.FeedbackActor;
import com.whz.feedback.model.feedback.FeedbackSubmitted;
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
 * This class is used as a provider for all command models.
 *
 * @since 1.0.0
 */
public class CommandModelJournalProvider {

  private static CommandModelJournalProvider instance;

  public final Journal<String> journal;

  /**
   * Get the singleton instance of the provider.
   *
   * @return instance of the provider
   * @since 1.0.0
   */
  public static CommandModelJournalProvider instance() {
    return instance;
  }

  /**
   * Reset the provider.
   *
   * @since 1.0.0
   */
  public static void reset() {
    instance = null;
  }

  /**
   * Initialize the command model with all necessary actors.
   *
   * @param stage stage of the current world
   * @param registry registry for {@link io.vlingo.lattice.model.sourcing.Sourced} types
   * @return initialized provider
   * @since 1.0.0
   */
  public static CommandModelJournalProvider using(
      final Stage stage, final SourcedTypeRegistry registry) {
    final Dispatcher noop =
        new Dispatcher() {
          @Override
          public void controlWith(final DispatcherControl control) {}

          @Override
          public void dispatch(Dispatchable d) {}
        };

    return using(stage, registry, noop);
  }

  /**
   * Initialize the command model with all necessary actors.
   *
   * @param stage stage of the current world
   * @param registry registry for {@link io.vlingo.lattice.model.sourcing.Sourced} types
   * @param dispatcher data holder for identity and state that has been successfully stored
   * @return initialized provider
   * @since 1.0.0
   */
  public static CommandModelJournalProvider using(
      final Stage stage, final SourcedTypeRegistry registry, final Dispatcher dispatcher) {
    if (instance != null) {
      return instance;
    }

    final EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(stage.world());

    entryAdapterProvider.registerAdapter(FeedbackSubmitted.class, new EventAdapter<>());

    final Journal<String> journal =
        StoreActorBuilder.from(
            stage, Model.COMMAND, dispatcher, StorageType.JOURNAL, Settings.properties(), true);

    registry.register(new Info(journal, FeedbackActor.class, FeedbackActor.class.getSimpleName()));

    instance = new CommandModelJournalProvider(journal);

    return instance;
  }

  /**
   * Create a provider for the command model.
   *
   * @param journal journal to store all actors
   * @since 1.0.0
   */
  private CommandModelJournalProvider(final Journal<String> journal) {
    this.journal = journal;
  }
}
