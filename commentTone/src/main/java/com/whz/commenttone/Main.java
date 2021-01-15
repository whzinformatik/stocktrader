/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.kb1prb13@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.whz.commenttone;

import com.whz.commenttone.model.CommentTone;
import com.whz.commenttone.rabbitmq.CommentTonePublisher;
import com.whz.commenttone.rabbitmq.CommentToneSubscriber;
import java.util.Optional;
import org.apache.log4j.BasicConfigurator;

public class Main {

  public static void main(String[] args) {
    final String serviceName =
        Optional.ofNullable(System.getenv("RABBITMQ_SERVICE")).orElse("localhost");

    final String publishExchangeName =
        Optional.ofNullable(System.getenv("RABBITMQ_PUBLISH_EXCHANGE")).orElse("commentTone");
    final String consumeExchangeName =
        Optional.ofNullable(System.getenv("RABBITMQ_CONSUME_EXCHANGE")).orElse("feedback");
    final String exchangeType =
        Optional.ofNullable(System.getenv("RABBITMQ_EXCHANGE_TYPE")).orElse("fanout");

    BasicConfigurator.configure();

    CommentToneSubscriber subscriber =
        new CommentToneSubscriber(serviceName, consumeExchangeName, exchangeType);
    CommentTonePublisher<CommentTone> publisher =
        new CommentTonePublisher<>(serviceName, publishExchangeName, exchangeType);

    subscriber.consume(publisher);
  }
}
