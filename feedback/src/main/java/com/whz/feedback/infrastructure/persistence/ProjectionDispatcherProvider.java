/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.infrastructure.persistence;

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

/**
 * This class is used as a provider for a projection dispatcher.
 *
 * @since 1.0.0
 */
public class ProjectionDispatcherProvider {

  private static ProjectionDispatcherProvider instance;

  public final ProjectionDispatcher projectionDispatcher;

  public final Dispatcher storeDispatcher;

  /**
   * Get the singleton instance of the provider.
   *
   * @return instance of the provider
   * @since 1.0.0
   */
  public static ProjectionDispatcherProvider instance() {
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
   * Initialize the projection dispatcher with all necessary events.
   *
   * @param stage stage of the current world
   * @return initialized provider
   * @since 1.0.0
   */
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

  /**
   * Create a provider for the projection dispatcher.
   *
   * @param storeDispatcher data holder for identity and state that has been successfully stored
   * @param projectionDispatcher defines the means of dispatching Projectable instances to
   *     Projections based on matching the Projections that handle descriptive causes
   * @since 1.0.0
   */
  private ProjectionDispatcherProvider(
      final Dispatcher storeDispatcher, final ProjectionDispatcher projectionDispatcher) {
    this.storeDispatcher = storeDispatcher;
    this.projectionDispatcher = projectionDispatcher;
  }
}
