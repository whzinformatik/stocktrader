package com.whz.commenttone.infrastructure.persistence;

import com.whz.commenttone.infrastructure.CommentToneData;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.State;
import io.vlingo.symbio.StateAdapter;

public class CommentToneDataStateAdapter implements StateAdapter<CommentToneData, State.TextState> {

    @Override
    public int typeVersion() {
        return 1;
    }

    @Override
    public CommentToneData fromRawState(final State.TextState raw) {
        return JsonSerialization.deserialized(raw.data, raw.typed());
    }

    @Override
    public State.TextState toRawState(String id, CommentToneData state, int stateVersion, Metadata metadata) {
        final String serialization = JsonSerialization.serialized(state);
        return new State.TextState(
                id, CommentToneData.class, typeVersion(), serialization, stateVersion, metadata);
    }

    @Override
    public State.TextState toRawState(CommentToneData state, int stateVersion, Metadata metadata) {
        final String serialization = JsonSerialization.serialized(state);
        return new State.TextState(
                state.id, CommentToneData.class, typeVersion(), serialization, stateVersion, metadata);
    }

    @Override
    public <ST> ST fromRawState(State.TextState raw, Class<ST> stateType) {
        return JsonSerialization.deserialized(raw.data, stateType);
    }
}
