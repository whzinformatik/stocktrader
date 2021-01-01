/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.utils;

import java.util.Optional;

/**
 * This class allows access to the environment variables.
 *
 * @since 1.0.0
 */
public enum EnvUtils {
  RABBITMQ_SERVICE("RABBITMQ_SERVICE", "localhost");

  /**
   * name of the environment variable
   *
   * @since 1.0.0
   */
  private final String name;

  /**
   * default value for the environment variable
   *
   * @since 1.0.0
   */
  private final String defaultValue;

  EnvUtils(String name, String defaultValue) {
    this.name = name;
    this.defaultValue = defaultValue;
  }

  /**
   * Get the value for the environment variable or the default value.
   *
   * @return default/value of the variable
   * @since 1.0.0
   */
  public String get() {
    return get(name, defaultValue);
  }

  /**
   * Get the value for an environment variable or a default value.
   *
   * @param name name of the environment variable
   * @param defaultValue default value if the variable is not set
   * @return default/value of the variable
   * @since 1.0.0
   */
  public static String get(String name, String defaultValue) {
    return Optional.ofNullable(System.getenv(name)).orElse(defaultValue);
  }
}
