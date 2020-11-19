package com.whz.commenttone.infrastructure.persistence;

import com.whz.commenttone.infrastructure.CommentToneData;
import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

import java.util.ArrayList;
import java.util.Collection;

public class CommentToneQueriesActor extends StateStoreQueryActor implements CommentToneQueries {

    public CommentToneQueriesActor(StateStore store) {
        super(store);
    }

    @Override
    public Completes<CommentToneData> commentToneOf(String id) {
        return queryStateFor(id, CommentToneData.class, CommentToneData.empty());
    }

    @Override
    public Completes<Collection<CommentToneData>> commentTones() {
        return streamAllOf(CommentToneData.class, new ArrayList<>());
    }

}
