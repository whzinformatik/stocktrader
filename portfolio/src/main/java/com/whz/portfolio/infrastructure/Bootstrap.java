/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.infrastructure;

import com.whz.portfolio.infrastructure.persistence.CommandModelJournalProvider;
import com.whz.portfolio.infrastructure.persistence.ProjectionDispatcherProvider;
import com.whz.portfolio.infrastructure.persistence.QueryModelStateStoreProvider;
import com.whz.portfolio.resource.PortfolioResource;
import com.whz.portfolio.resource.StockAcquiredPublisher;
import com.whz.portfolio.resource.StockQuoteSubscriber;
import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.http.resource.Configuration.Sizing;
import io.vlingo.http.resource.Configuration.Timing;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;

/**
 * Generated class by 'VLINGO/XOOM Starter'.
 *
 * @since 1.0.0
 */
public class Bootstrap {

  private static final Logger logger = Logger.basicLogger();

  private static final int DEFAULT_PORT = 18082;
  private static final String NAME = "portfolio";

  private static Bootstrap instance;

  private final Server server;
  private final World world;

  public Bootstrap(final int port) throws Exception {
    world = World.startWithDefaults(NAME);

    final Stage stage = world.stageNamed(NAME);

    final SourcedTypeRegistry sourcedTypeRegistry = new SourcedTypeRegistry(world);
    final StatefulTypeRegistry statefulTypeRegistry = new StatefulTypeRegistry(world);
    QueryModelStateStoreProvider.using(stage, statefulTypeRegistry);
    CommandModelJournalProvider.using(
        stage, sourcedTypeRegistry, ProjectionDispatcherProvider.using(stage).storeDispatcher);

    final PortfolioResource portfolioResource = new PortfolioResource(stage);
    Resources allResources = Resources.are(portfolioResource.routes());

    server = Server.startWith(stage, allResources, port, Sizing.define(), Timing.define());

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  if (instance != null) {
                    instance.server.stop();
                    StockQuoteSubscriber.INSTANCE.stop();
                    StockAcquiredPublisher.INSTANCE.stop();
                    logger.info("\n");
                    logger.info("=========================");
                    logger.info("Stopping {}.", NAME);
                    logger.info("=========================");
                  }
                }));
  }

  void stopServer() throws Exception {
    if (instance == null) {
      throw new IllegalStateException("Schemata server not running");
    }
    instance.server.stop();
  }

  public static void main(final String[] args) throws Exception {
    logger.info("=========================");
    logger.info("service: {}.", NAME);
    logger.info("=========================");

    int port;

    try {
      port = Integer.parseInt(args[0]);
    } catch (Exception e) {
      port = DEFAULT_PORT;
      logger.warn("portfolio: Command line does not provide a valid port; defaulting to: {}", port);
    }

    instance = new Bootstrap(port);
  }

  public Server getServer() {
    return server;
  }
}
