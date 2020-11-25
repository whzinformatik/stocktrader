package com.whz.feedback.infrastructure.persistence;

/*-
 * #%L
 * feedback
 * %%
 * Copyright (C) 2020 Fachgruppe Informatik WHZ
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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

public class QueryModelStateStoreProvider {

  private static QueryModelStateStoreProvider instance;

  public final StateStore store;

  public final FeedbackQueries feedbackQueries;

  public static QueryModelStateStoreProvider instance() {
    return instance;
  }

  public static void reset() {
    instance = null;
  }

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

  private QueryModelStateStoreProvider(final Stage stage, final StateStore store) {
    this.store = store;
    this.feedbackQueries = stage.actorFor(FeedbackQueries.class, FeedbackQueriesActor.class, store);
  }
}
