/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.infrastructure.persistence;

import com.whz.feedback.infrastructure.FeedbackData;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.StateAdapterProvider;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.StateTypeStateStoreMap;
import io.vlingo.xoom.actors.Settings;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;
import io.vlingo.xoom.storage.Model;
import io.vlingo.xoom.storage.StoreActorBuilder;

/**
 * This class is used as a provider for all query model state stores.
 * @since 1.0.0
 */
public class QueryModelStateStoreProvider {

  private static QueryModelStateStoreProvider instance;

  public final StateStore store;

  public final FeedbackQueries feedbackQueries;

  /**
   * Get the singleton instance of the provider.
   * @return instance of the provider
   * @since 1.0.0
   */
  public static QueryModelStateStoreProvider instance() {
    return instance;
  }

  /**
   * Reset the provider.
   * @since 1.0.0
   */
  public static void reset() {
    instance = null;
  }

  /**
   * Initialize the query model state store with all necessary DTOs.
   * @param stage stage of the current world
   * @param registry registry for StatefulEntity type
   * @return initialized provider
   * @since 1.0.0
   */
  public static QueryModelStateStoreProvider using(
      final Stage stage, final StatefulTypeRegistry registry) {
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
   * Initialize the query model state store with all necessary DTOs.
   * @param stage stage of the current world
   * @param registry registry for StatefulEntity type
   * @param dispatcher data holder for identity and state that has been successfully stored
   * @return initialized provider
   * @since 1.0.0
   */
  @SuppressWarnings("rawtypes")
  public static QueryModelStateStoreProvider using(
      final Stage stage, final StatefulTypeRegistry registry, final Dispatcher dispatcher) {
    if (instance != null) {
      return instance;
    }

    new EntryAdapterProvider(stage.world()); // future use

    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider((stage.world()));
    stateAdapterProvider.registerAdapter(FeedbackData.class, new FeedbackDataStateAdapter());

    StateTypeStateStoreMap.stateTypeToStoreName(
        FeedbackData.class, FeedbackData.class.getSimpleName());

    final StateStore store =
        StoreActorBuilder.from(
            stage, Model.QUERY, dispatcher, StorageType.STATE_STORE, Settings.properties(), true);

    registry.register(
        new StatefulTypeRegistry.Info(
            store, FeedbackData.class, FeedbackData.class.getSimpleName()));

    instance = new QueryModelStateStoreProvider(stage, store);

    return instance;
  }

  /**
   * Create a provider for the query model state store.
   * @param stage stage of the current world
   * @param store instance of basic state store
   * @since 1.0.0
   */
  private QueryModelStateStoreProvider(final Stage stage, final StateStore store) {
    this.store = store;
    this.feedbackQueries = stage.actorFor(FeedbackQueries.class, FeedbackQueriesActor.class, store);
  }
}
