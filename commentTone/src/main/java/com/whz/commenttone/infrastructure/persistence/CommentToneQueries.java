package com.whz.commenttone.infrastructure.persistence;

import com.whz.commenttone.infrastructure.CommentToneData;
import io.vlingo.common.Completes;

import java.util.Collection;

public interface CommentToneQueries {
    Completes<CommentToneData> commentToneOf(String id);

    Completes<Collection<CommentToneData>> commentTones();
}