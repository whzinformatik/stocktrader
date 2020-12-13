package com.whz.commenttone;

import com.whz.commenttone.rabbitmq.CommentToneSubscriber;

public class Main {

    public static void main(String[] args) {
        CommentToneSubscriber subscriber = new CommentToneSubscriber();

        subscriber.consume();
    }
}
