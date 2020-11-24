package com.whz.feedback.infrastructure.persistence;

import com.whz.feedback.model.feedback.FeedbackSubmittedEvent;
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

public class CommandModelJournalProvider {

  private static CommandModelJournalProvider instance;

  public final Journal<String> journal;

  public static CommandModelJournalProvider instance() {
    return instance;
  }

  public static void reset() {
    instance = null;
  }

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

  public static CommandModelJournalProvider using(
      final Stage stage, final SourcedTypeRegistry registry, final Dispatcher dispatcher) {
    if (instance != null) {
      return instance;
    }

    final EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(stage.world());

    entryAdapterProvider.registerAdapter(FeedbackSubmittedEvent.class, new EventAdapter<>());

    final Journal<String> journal =
        StoreActorBuilder.from(
            stage, Model.COMMAND, dispatcher, StorageType.JOURNAL, Settings.properties(), true);

    registry.register(
        new Info(
            journal, FeedbackSubmittedEvent.class, FeedbackSubmittedEvent.class.getSimpleName()));

    instance = new CommandModelJournalProvider(journal);

    return instance;
  }

  private CommandModelJournalProvider(final Journal<String> journal) {
    this.journal = journal;
  }
}
