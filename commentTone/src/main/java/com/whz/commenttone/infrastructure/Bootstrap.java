package com.whz.commenttone.infrastructure;

import com.whz.commenttone.infrastructure.persistence.CommandModelJournalProvider;
import com.whz.commenttone.infrastructure.persistence.ProjectionDispatcherProvider;
import com.whz.commenttone.infrastructure.persistence.QueryModelStateStoreProvider;
import com.whz.commenttone.resource.CommentToneResource;
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
    private static final int DefaultPort = 18080;
    private static Bootstrap instance;
    private final Server server;
    private final World world;

    public Bootstrap(final int port) throws Exception {
        world = World.startWithDefaults("commentTone");

        final Stage stage =
                world.stageNamed("commentTone", Stage.class, new GridAddressFactory(IdentityGeneratorType.RANDOM));

        final SourcedTypeRegistry sourcedTypeRegistry = new SourcedTypeRegistry(world);
        final StatefulTypeRegistry statefulTypeRegistry = new StatefulTypeRegistry(world);
        QueryModelStateStoreProvider.using(stage, statefulTypeRegistry);
        CommandModelJournalProvider.using(stage, sourcedTypeRegistry, ProjectionDispatcherProvider.using(stage).storeDispatcher);

        final CommentToneResource commentToneResource = new CommentToneResource(stage);

        Resources allResources = Resources.are(
                commentToneResource.routes()
        );

        server = Server.startWith(stage, allResources, port, Sizing.define(), Timing.define());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (instance != null) {
                instance.server.stop();

                System.out.println("\n");
                System.out.println("=========================");
                System.out.println("Stopping commentTone.");
                System.out.println("=========================");
            }
        }));
    }

    public static void main(final String[] args) throws Exception {
        System.out.println("=========================");
        System.out.println("service: commentTone.");
        System.out.println("=========================");

        int port;

        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            port = DefaultPort;
            System.out.println("commentTone: Command line does not provide a valid port; defaulting to: " + port);
        }

        instance = new Bootstrap(port);
    }

    void stopServer() throws Exception {
        if (instance == null) {
            throw new IllegalStateException("Schemata server not running");
        }
        instance.server.stop();
    }
}
