package com.whz.account.infrastructure.persistence;

import java.util.List;

import com.whz.account.model.account.AccountPlaceholderDefined;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.xoom.actors.Settings;
import io.vlingo.xoom.storage.Model;
import io.vlingo.xoom.storage.StoreActorBuilder;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;

public class CommandModelJournalProvider  {
  private static CommandModelJournalProvider instance;

  public final Journal<String> journal;

  public static CommandModelJournalProvider  instance() {
    return instance;
  }

  public static CommandModelJournalProvider using(final Stage stage, final SourcedTypeRegistry registry) {
    final Dispatcher noop = new Dispatcher() {
      public void controlWith(final DispatcherControl control) { }
      public void dispatch(Dispatchable d) { }
    };

    return using(stage, registry, noop);
 }

  @SuppressWarnings({ "unchecked", "unused" })
  public static CommandModelJournalProvider using(final Stage stage, final SourcedTypeRegistry registry, final Dispatcher dispatcher) {
    if (instance != null) {
      return instance;
    }

    final EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(stage.world());

    entryAdapterProvider.registerAdapter(AccountPlaceholderDefined.class, new AccountPlaceholderDefinedAdapter());

    final Journal<String> journal =
              StoreActorBuilder.from(stage, Model.COMMAND, dispatcher, StorageType.JOURNAL, Settings.properties(), true);

    registry.register(new Info(journal, AccountPlaceholderDefined.class, AccountPlaceholderDefined.class.getSimpleName()));

    instance = new CommandModelJournalProvider(journal);

    return instance;
  }

  private CommandModelJournalProvider(final Journal<String> journal) {
    this.journal = journal;
  }

}
