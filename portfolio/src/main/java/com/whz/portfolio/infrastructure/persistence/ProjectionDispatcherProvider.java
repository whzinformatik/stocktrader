/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.infrastructure.persistence;

import com.whz.portfolio.model.portfolio.PortfolioCreated;
import com.whz.portfolio.model.portfolio.StockAcquired;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Protocols;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.projection.ProjectionDispatcher;
import io.vlingo.lattice.model.projection.ProjectionDispatcher.ProjectToDescription;
import io.vlingo.lattice.model.projection.TextProjectionDispatcherActor;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import java.util.Arrays;
import java.util.List;

/**
 * Generated class by 'VLINGO/XOOM Starter'. The event types have to be added to the 'descriptions'
 * list.
 *
 * @since 1.0.0
 */
@SuppressWarnings("rawtypes")
public class ProjectionDispatcherProvider {
  private static ProjectionDispatcherProvider instance;

  public final ProjectionDispatcher projectionDispatcher;
  public final Dispatcher storeDispatcher;

  public static ProjectionDispatcherProvider instance() {
    return instance;
  }

  public static ProjectionDispatcherProvider using(final Stage stage) {
    if (instance != null) return instance;

    final List<ProjectToDescription> descriptions =
        Arrays.asList(
            ProjectToDescription.with(
                PortfolioProjectionActor.class, PortfolioCreated.class.getName()),
            ProjectToDescription.with(
                PortfolioProjectionActor.class, StockAcquired.class.getName()));

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

  public static void reset() {
    instance = null;
  }
}
