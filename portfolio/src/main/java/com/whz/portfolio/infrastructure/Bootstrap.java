package com.whz.portfolio.infrastructure;

import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import com.whz.portfolio.infrastructure.persistence.ProjectionDispatcherProvider;
import com.whz.portfolio.resource.PortfolioResource;
import com.whz.portfolio.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;

import org.flywaydb.core.Flyway;

import com.whz.portfolio.infrastructure.persistence.CommandModelJournalProvider;

import io.vlingo.actors.GridAddressFactory;
import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.common.identity.IdentityGeneratorType;
import io.vlingo.http.resource.Configuration.Sizing;
import io.vlingo.http.resource.Configuration.Timing;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;

public class Bootstrap {

	  private static final Logger logger = Logger.basicLogger();

	  private static final int DEFAULT_PORT = 18082;
	  private static final String NAME = "portfolio";

	  private static Bootstrap instance;

	  private final Server server;
	  private final World world;

	  public Bootstrap(final int port) throws Exception {
	    Flyway.configure().dataSource("jdbc:postgresql://[::1]:5432/", "vlingo_test", "vlingo123").load().migrate();

	    world = World.startWithDefaults(NAME);

	    final Stage stage =
	            world.stageNamed(NAME, Stage.class, new GridAddressFactory(IdentityGeneratorType.RANDOM));

	    final SourcedTypeRegistry sourcedTypeRegistry = new SourcedTypeRegistry(world);
	    final StatefulTypeRegistry statefulTypeRegistry = new StatefulTypeRegistry(world);
	    QueryModelStateStoreProvider.using(stage, statefulTypeRegistry);
	    CommandModelJournalProvider.using(stage, sourcedTypeRegistry, ProjectionDispatcherProvider.using(stage).storeDispatcher);

	    final PortfolioResource feedbackAggregateResource = new PortfolioResource(stage);
	    Resources allResources = Resources.are(
	        feedbackAggregateResource.routes()
	    );

	    server = Server.startWith(stage, allResources, port, Sizing.define(), Timing.define());

	    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	      if (instance != null) {
	        instance.server.stop();

	        logger.info("\n");
	        logger.info("=========================");
	        logger.info("Stopping portfolio.");
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
	    logger.info("service: portfolio.");
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
	}
