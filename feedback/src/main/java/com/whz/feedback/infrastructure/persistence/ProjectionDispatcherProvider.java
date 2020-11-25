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

import com.whz.feedback.model.feedback.FeedbackSubmitted;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Protocols;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.projection.ProjectionDispatcher;
import io.vlingo.lattice.model.projection.ProjectionDispatcher.ProjectToDescription;
import io.vlingo.lattice.model.projection.TextProjectionDispatcherActor;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import java.util.Collections;
import java.util.List;

public class ProjectionDispatcherProvider {

  private static ProjectionDispatcherProvider instance;

  public final ProjectionDispatcher projectionDispatcher;

  public final Dispatcher storeDispatcher;

  public static ProjectionDispatcherProvider instance() {
    return instance;
  }

  public static void reset() {
    instance = null;
  }

  public static ProjectionDispatcherProvider using(final Stage stage) {
    if (instance != null) return instance;

    final List<ProjectToDescription> descriptions =
        Collections.singletonList(
            ProjectToDescription.with(
                FeedbackProjectionActor.class, FeedbackSubmitted.class.getName()));

    final Protocols dispatcherProtocols =
        stage.actorFor(
            new Class<?>[] {Dispatcher.class, ProjectionDispatcher.class},
            Definition.has(
                TextProjectionDispatcherActor.class, Definition.parameters(descriptions)));

    final Protocols.Two<Dispatcher, ProjectionDispatcher> dispatchers =
        Protocols.two(dispatcherProtocols);

    instance = new ProjectionDispatcherProvider(dispatchers._1, dispatchers._2);

    return instance;
  }

  private ProjectionDispatcherProvider(
      final Dispatcher storeDispatcher, final ProjectionDispatcher projectionDispatcher) {
    this.storeDispatcher = storeDispatcher;
    this.projectionDispatcher = projectionDispatcher;
  }
}
