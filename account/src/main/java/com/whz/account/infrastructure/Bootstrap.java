/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.infrastructure;

import com.whz.account.infrastructure.persistence.CommandModelJournalProvider;
import com.whz.account.infrastructure.persistence.ProjectionDispatcherProvider;
import com.whz.account.infrastructure.persistence.QueryModelStateStoreProvider;
import com.whz.account.resource.AccountResource;
import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.http.resource.Configuration.Sizing;
import io.vlingo.http.resource.Configuration.Timing;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;

/** @since 1.0.0 */
public class Bootstrap {

  private static final Logger logger = Logger.basicLogger();

  private static Bootstrap instance;
  private static final int DefaultPort = 18084;
  private static final String NAME = "account";

  private final Server server;
  private final World world;

  /** @since 1.0.0 */
  public Bootstrap(final int port) throws Exception {
    world = World.startWithDefaults(NAME);

    final Stage stage = world.stageNamed(NAME);

    final SourcedTypeRegistry sourcedTypeRegistry = new SourcedTypeRegistry(world);
    final StatefulTypeRegistry statefulTypeRegistry = new StatefulTypeRegistry(world);
    QueryModelStateStoreProvider.using(stage, statefulTypeRegistry);
    CommandModelJournalProvider.using(
        stage, sourcedTypeRegistry, ProjectionDispatcherProvider.using(stage).storeDispatcher);

    final AccountResource accountResource = new AccountResource(stage);

    Resources allResources = Resources.are(accountResource.routes());

    server = Server.startWith(stage, allResources, port, Sizing.define(), Timing.define());

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  if (instance != null) {
                    instance.server.stop();

                    logger.info("\n");
                    logger.info("=========================");
                    logger.info("Stopping account.");
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
    logger.info("service: account.");
    logger.info("=========================");

    int port;

    try {
      port = Integer.parseInt(args[0]);
    } catch (Exception e) {
      port = DefaultPort;
      logger.warn("account: Command line does not provide a valid port; defaulting to: " + port);
    }

    instance = new Bootstrap(port);
  }
}
