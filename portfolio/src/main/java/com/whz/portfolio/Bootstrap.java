package com.whz.portfolio;

import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.http.resource.Configuration.Sizing;
import io.vlingo.http.resource.Configuration.Timing;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.XoomInitializationAware;
import io.vlingo.xoom.annotation.initializer.ResourceHandlers;
import io.vlingo.xoom.annotation.initializer.Xoom;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;

import com.whz.portfolio.infrastructure.persistence.CommandModelJournalProvider;
import com.whz.portfolio.infrastructure.persistence.ProjectionDispatcherProvider;
import com.whz.portfolio.infrastructure.persistence.QueryModelStateStoreProvider;
import com.whz.portfolio.resource.PortfolioResource;

@Xoom(name = "portfolio")
@ResourceHandlers(packages = "com.whz.portfolio.resource")
public class Bootstrap implements XoomInitializationAware {

	@Override
	public void onInit(final Stage stage) {
		final StatefulTypeRegistry registry = new StatefulTypeRegistry(stage.world());
		final SourcedTypeRegistry sourcedTypeRegistry = new SourcedTypeRegistry(stage.world());
		QueryModelStateStoreProvider.using(stage, registry);
		CommandModelJournalProvider.using(stage, sourcedTypeRegistry,
				ProjectionDispatcherProvider.using(stage).storeDispatcher);
	}

//	private final static int PORT = 8080;
//	private static Bootstrap instance;
//
//	public final Server server;
//	public final World world;
//
//	private Bootstrap() {
//		this.world = World.startWithDefaults("portfolio service");
//
//		// TODO
//		final Resources resources = Resources.are(new PortfolioResource(null).routes());
//
//		this.server = Server.startWith(world.stage(), resources, PORT, Sizing.define(), Timing.define());
//	}
//
//	public static final Bootstrap instance() {
//		if (instance == null) {
//			instance = new Bootstrap();
//		}
//		return instance;
//	}
//
//	public static void main(final String[] args) throws Exception {
//		System.out.println("=========================================");
//		System.out.println("service: started at http://localhost:" + Bootstrap.PORT);
//		System.out.println("try: curl http://localhost:" + Bootstrap.PORT + "/portfolio");
//		System.out.println("=========================================");
//		Bootstrap.instance();
//	}
}
