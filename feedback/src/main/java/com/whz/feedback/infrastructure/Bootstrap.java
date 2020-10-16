package com.whz.feedback.infrastructure;

import com.whz.feedback.infrastructure.persistence.QueryModelStateStoreProvider;
import com.whz.feedback.infrastructure.persistence.CommandModelJournalProvider;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import com.whz.feedback.resource.FeedbackResource;
import com.whz.feedback.infrastructure.persistence.ProjectionDispatcherProvider;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;

import io.vlingo.actors.GridAddressFactory;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.common.identity.IdentityGeneratorType;
import io.vlingo.http.resource.Configuration.Sizing;
import io.vlingo.http.resource.Configuration.Timing;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;
import io.vlingo.xoom.Boot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bootstrap {

  private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

  private static final int DEFAULT_PORT = 18080;
  private static final String NAME = "feedback";

  private static Bootstrap instance;

  private final Server server;
  private final World world;

  public Bootstrap(final int port) throws Exception {
    world = World.startWithDefaults(NAME);

    final Stage stage =
            world.stageNamed(NAME, Stage.class, new GridAddressFactory(IdentityGeneratorType.RANDOM));

    final SourcedTypeRegistry sourcedTypeRegistry = new SourcedTypeRegistry(world);
    final StatefulTypeRegistry statefulTypeRegistry = new StatefulTypeRegistry(world);
    QueryModelStateStoreProvider.using(stage, statefulTypeRegistry);
    CommandModelJournalProvider.using(stage, sourcedTypeRegistry, ProjectionDispatcherProvider.using(stage).storeDispatcher);

    final FeedbackResource feedbackAggregateResource = new FeedbackResource(stage);
    Resources allResources = Resources.are(
        feedbackAggregateResource.routes()
    );

    server = Server.startWith(stage, allResources, port, Sizing.define(), Timing.define());

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      if (instance != null) {
        instance.server.stop();

        LOG.info("\n");
        LOG.info("=========================");
        LOG.info("Stopping feedback.");
        LOG.info("=========================");
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
    LOG.info("=========================");
    LOG.info("service: feedback.");
    LOG.info("=========================");

    int port;

    try {
      port = Integer.parseInt(args[0]);
    } catch (Exception e) {
      port = DEFAULT_PORT;
      LOG.warn("feedback: Command line does not provide a valid port; defaulting to: {}", port);
    }

    instance = new Bootstrap(port);
  }
}
