package com.whz.account.infrastructure;

import org.flywaydb.core.Flyway;

import com.whz.account.infrastructure.persistence.CommandModelJournalProvider;
import com.whz.account.infrastructure.persistence.ProjectionDispatcherProvider;
import com.whz.account.infrastructure.persistence.QueryModelStateStoreProvider;
import com.whz.account.resource.AccountResource;

import io.vlingo.actors.GridAddressFactory;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.common.identity.IdentityGeneratorType;
import io.vlingo.http.resource.Configuration.Sizing;
import io.vlingo.http.resource.Configuration.Timing;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;

public class Bootstrap {
	private static Bootstrap instance;
	private static int DefaultPort = 18084;

	private final Server server;
	private final World world;

	public Bootstrap(final int port) throws Exception {
		Flyway.configure().dataSource("jdbc:postgresql://[::1]:5432/", "vlingo_test", "vlingo123").load().migrate();

		world = World.startWithDefaults("account");

		final Stage stage = world.stageNamed("account", Stage.class,
				new GridAddressFactory(IdentityGeneratorType.RANDOM));

		final SourcedTypeRegistry sourcedTypeRegistry = new SourcedTypeRegistry(world);
		final StatefulTypeRegistry statefulTypeRegistry = new StatefulTypeRegistry(world);
		QueryModelStateStoreProvider.using(stage, statefulTypeRegistry);
		CommandModelJournalProvider.using(stage, sourcedTypeRegistry,
				ProjectionDispatcherProvider.using(stage).storeDispatcher);

		final AccountResource accountResource = new AccountResource(stage);

		Resources allResources = Resources.are(accountResource.routes());

		server = Server.startWith(stage, allResources, port, Sizing.define(), Timing.define());

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (instance != null) {
				instance.server.stop();

				System.out.println("\n");
				System.out.println("=========================");
				System.out.println("Stopping account.");
				System.out.println("=========================");
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
		System.out.println("=========================");
		System.out.println("service: account.");
		System.out.println("=========================");

		int port;

		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			port = DefaultPort;
			System.out.println("account: Command line does not provide a valid port; defaulting to: " + port);
		}

		instance = new Bootstrap(port);
	}
}
