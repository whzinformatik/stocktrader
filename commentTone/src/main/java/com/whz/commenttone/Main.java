/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.kb1prb13@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.whz.commenttone;

import com.whz.commenttone.rabbitmq.CommentToneSubscriber;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {

  public static void main(String[] args) {
    CommentToneSubscriber subscriber = new CommentToneSubscriber();

    try {
      subscriber.consume();
    } catch (IOException | TimeoutException ioException) {
      ioException.printStackTrace();
    }
  }
}
