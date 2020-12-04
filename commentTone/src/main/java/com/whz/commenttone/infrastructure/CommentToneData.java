package com.whz.commenttone.infrastructure;

import com.whz.commenttone.model.commenttone.CommentToneState;

import java.util.ArrayList;
import java.util.List;

public class CommentToneData {
    public final String id;
    public final String message;
    public final String sentiment;

    private CommentToneData(final String id, final String message, final String sentiment) {
        this.id = id;
        this.message = message;
        this.sentiment = sentiment;
    }

    public static CommentToneData empty() {
        return new CommentToneData("", "", "");
    }

    public static CommentToneData from(final CommentToneState state) {
        return new CommentToneData(state.id, state.message, state.sentiment);
    }

    public static List<CommentToneData> from(final List<CommentToneState> states) {
        final List<CommentToneData> data = new ArrayList<>(states.size());

        for (final CommentToneState state : states) {
            data.add(CommentToneData.from(state));
        }

        return data;
    }

    public static CommentToneData from(final String id, final String message, final String sentiment) {
        return new CommentToneData(id, message, sentiment);
    }

    public static CommentToneData just(final String message, final String sentiment) {
        return new CommentToneData("", message, sentiment);
    }

    @Override
    public String toString() {
        return "CommentToneData{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", sentiment='" + sentiment + '\'' +
                '}';
    }
}
