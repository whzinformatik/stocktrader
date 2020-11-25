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

    entryAdapterProvider.registerAdapter(FeedbackSubmitted.class, new EventAdapter<>());

    final Journal<String> journal =
        StoreActorBuilder.from(
            stage, Model.COMMAND, dispatcher, StorageType.JOURNAL, Settings.properties(), true);

    registry.register(new Info(journal, FeedbackActor.class, FeedbackActor.class.getSimpleName()));

    instance = new CommandModelJournalProvider(journal);

    return instance;
  }

  private CommandModelJournalProvider(final Journal<String> journal) {
    this.journal = journal;
  }
}
