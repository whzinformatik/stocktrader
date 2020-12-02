package com.whz.commenttone.infrastructure.persistence;

import com.whz.commenttone.model.commenttone.CommentTonePublishedEvent;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Protocols;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.projection.ProjectionDispatcher;
import io.vlingo.lattice.model.projection.ProjectionDispatcher.ProjectToDescription;
import io.vlingo.lattice.model.projection.TextProjectionDispatcherActor;
import io.vlingo.symbio.store.dispatch.Dispatcher;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ProjectionDispatcherProvider {
    private static ProjectionDispatcherProvider instance;

    public final ProjectionDispatcher projectionDispatcher;
    public final Dispatcher storeDispatcher;

    public static ProjectionDispatcherProvider instance() {
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    public static ProjectionDispatcherProvider using(final Stage stage) {
        if (instance != null) return instance;

        final List<ProjectToDescription> descriptions =
                Collections.singletonList(
                        ProjectToDescription.with(CommentToneProjectionActor.class, CommentTonePublishedEvent.class.getName())
                );

        final Protocols dispatcherProtocols =
                stage.actorFor(
                        new Class<?>[]{Dispatcher.class, ProjectionDispatcher.class},
                        Definition.has(TextProjectionDispatcherActor.class, Definition.parameters(descriptions)));

        final Protocols.Two<Dispatcher, ProjectionDispatcher> dispatchers = Protocols.two(dispatcherProtocols);

        instance = new ProjectionDispatcherProvider(dispatchers._1, dispatchers._2);

        return instance;
    }

    private ProjectionDispatcherProvider(final Dispatcher storeDispatcher, final ProjectionDispatcher projectionDispatcher) {
        this.storeDispatcher = storeDispatcher;
        this.projectionDispatcher = projectionDispatcher;
    }
}
