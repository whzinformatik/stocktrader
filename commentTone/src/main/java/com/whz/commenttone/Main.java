/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.kb1prb13@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.whz.commenttone;

import com.whz.commenttone.rabbitmq.CommentToneSubscriber;

public class Main {

  public static void main(String[] args) {
    CommentToneSubscriber subscriber = new CommentToneSubscriber();

    subscriber.consume();
  }
}
