package com.whz.commenttone.infrastructure;

import com.whz.commenttone.model.commenttone.CommentToneState;

import java.util.ArrayList;
import java.util.List;

public class CommentToneData {
    public final String id;
    public final String message;

    private CommentToneData(final String id, final String message) {
        this.id = id;
        this.message = message;
    }

    public static CommentToneData empty() {
        return new CommentToneData("", "");
    }

    public static CommentToneData from(final CommentToneState state) {
        return new CommentToneData(state.id, state.message);
    }

    public static List<CommentToneData> from(final List<CommentToneState> states) {
        final List<CommentToneData> data = new ArrayList<>(states.size());

        for (final CommentToneState state : states) {
            data.add(CommentToneData.from(state));
        }

        return data;
    }

    public static CommentToneData from(final String id, final String message) {
        return new CommentToneData(id, message);
    }

    public static CommentToneData just(final String message) {
        return new CommentToneData("", message);
    }

    @Override
    public String toString() {
        return "CommentToneData [id=" + id + " message=" + message + "]";
    }
}
