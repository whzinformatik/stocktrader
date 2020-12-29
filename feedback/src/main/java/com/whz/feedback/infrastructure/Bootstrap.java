/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.infrastructure;

import com.whz.feedback.infrastructure.persistence.CommandModelJournalProvider;
import com.whz.feedback.infrastructure.persistence.ProjectionDispatcherProvider;
import com.whz.feedback.infrastructure.persistence.QueryModelStateStoreProvider;
import com.whz.feedback.resource.FeedbackResource;
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
 * This class represents the main class for the entire feedback application. It can be used to start
 * the program.
 *
 * @since 1.0.0
 */
public class Bootstrap {

  /** standard port for the application */
  private static final int DEFAULT_PORT = 18080;

  /** name of the application */
  private static final String NAME = "feedback";

  private static final Logger logger = Logger.basicLogger();

  private static Bootstrap instance;

  private final Server server;

  private final World world;

  /**
   * Initialize the program and start the server.
   *
   * @param port port to reach the application
   * @throws Exception if something went wrong
   * @since 1.0.0
   */
  public Bootstrap(final int port) throws Exception {
    world = World.startWithDefaults(NAME);

    final Stage stage = world.stageNamed(NAME);

    final SourcedTypeRegistry sourcedTypeRegistry = new SourcedTypeRegistry(world);
    final StatefulTypeRegistry statefulTypeRegistry = new StatefulTypeRegistry(world);
    QueryModelStateStoreProvider.using(stage, statefulTypeRegistry);
    CommandModelJournalProvider.using(
        stage, sourcedTypeRegistry, ProjectionDispatcherProvider.using(stage).storeDispatcher);

    final FeedbackResource feedbackResource = new FeedbackResource(stage);
    Resources allResources = Resources.are(feedbackResource.routes());

    server = Server.startWith(stage, allResources, port, Sizing.define(), Timing.define());

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  if (instance != null) {
                    instance.server.stop();

                    logger.info("\n");
                    logger.info("=========================");
                    logger.info("Stopping {}.", NAME);
                    logger.info("=========================");
                  }
                }));
  }

  /**
   * Shutting down the server and releasing occupied resources.
   *
   * @throws Exception if the instance could not be created
   * @since 1.0.0
   */
  void stopServer() throws Exception {
    if (instance == null) {
      throw new IllegalStateException("Schemata server not running");
    }
    instance.server.stop();
  }

  public Server getServer() {
    return server;
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
      logger.warn("{}: Command line does not provide a valid port; defaulting to: {}", NAME, port);
    }

    instance = new Bootstrap(port);
  }
}
