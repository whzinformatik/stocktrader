package com.whz.feedback.infrastructure;

import com.whz.feedback.infrastructure.persistence.CommandModelJournalProvider;
import com.whz.feedback.infrastructure.persistence.ProjectionDispatcherProvider;
import com.whz.feedback.infrastructure.persistence.QueryModelStateStoreProvider;
import com.whz.feedback.resource.FeedbackResource;
import io.vlingo.actors.GridAddressFactory;
import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.common.identity.IdentityGeneratorType;
import io.vlingo.http.resource.Configuration.Sizing;
import io.vlingo.http.resource.Configuration.Timing;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import org.flywaydb.core.Flyway;

public class Bootstrap {

  private static final Logger logger = Logger.basicLogger();

  private static final int DEFAULT_PORT = 18080;

  private static final String NAME = "feedback";

  private static Bootstrap instance;

  private final Server server;

  private final World world;

  public Bootstrap(final int port) throws Exception {
    Flyway.configure()
        .dataSource("jdbc:postgresql://[::1]:5432/", "vlingo_test", "vlingo123")
        .load()
        .migrate();

    world = World.startWithDefaults(NAME);

    final Stage stage =
        world.stageNamed(NAME, Stage.class, new GridAddressFactory(IdentityGeneratorType.RANDOM));

    final SourcedTypeRegistry sourcedTypeRegistry = new SourcedTypeRegistry(world);
    final StatefulTypeRegistry statefulTypeRegistry = new StatefulTypeRegistry(world);
    QueryModelStateStoreProvider.using(stage, statefulTypeRegistry);
    CommandModelJournalProvider.using(
        stage, sourcedTypeRegistry, ProjectionDispatcherProvider.using(stage).storeDispatcher);

    final FeedbackResource feedbackAggregateResource = new FeedbackResource(stage);
    Resources allResources = Resources.are(feedbackAggregateResource.routes());

    server = Server.startWith(stage, allResources, port, Sizing.define(), Timing.define());

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  if (instance != null) {
                    instance.server.stop();

                    logger.info("\n");
                    logger.info("=========================");
                    logger.info("Stopping feedback.");
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

  public Server getServer() {
    return server;
  }

  public static void main(final String[] args) throws Exception {
    logger.info("=========================");
    logger.info("service: feedback.");
    logger.info("=========================");

    int port;

    try {
      port = Integer.parseInt(args[0]);
    } catch (Exception e) {
      port = DEFAULT_PORT;
      logger.warn("feedback: Command line does not provide a valid port; defaulting to: {}", port);
    }

    instance = new Bootstrap(port);
  }
}
