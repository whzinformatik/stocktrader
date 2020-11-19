package com.whz.commenttone.infrastructure.persistence;

import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.xoom.actors.Settings;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;
import io.vlingo.xoom.storage.Model;
import io.vlingo.xoom.storage.StoreActorBuilder;

public class QueryModelStateStoreProvider {
    private static QueryModelStateStoreProvider instance;

    public final StateStore store;
    public final CommentToneQueries commentToneQueries;

    @SuppressWarnings({"unchecked", "rawtypes"})
    private QueryModelStateStoreProvider(final Stage stage, final StateStore store) {
        this.store = store;
        this.commentToneQueries = stage.actorFor(CommentToneQueries.class, CommentToneQueriesActor.class, store);
    }

    public static QueryModelStateStoreProvider instance() {
        return instance;
    }

    public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry) {
        final Dispatcher noop = new Dispatcher() {
            public void controlWith(final DispatcherControl control) {
            }

            public void dispatch(Dispatchable d) {
            }
        };

        return using(stage, registry, noop);
    }

    @SuppressWarnings("rawtypes")
    public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher dispatcher) {
        if (instance != null) {
            return instance;
        }

        new EntryAdapterProvider(stage.world()); // future use

        final StateStore store =
                StoreActorBuilder.from(stage, Model.QUERY, dispatcher, StorageType.STATE_STORE, Settings.properties(), true);

        instance = new QueryModelStateStoreProvider(stage, store);

        return instance;
    }
}
