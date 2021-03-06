/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.infrastructure;

/**
 * EventTypes that are available for the account microservice.
 *
 * @since 1.0.0
 */
public enum EventTypes {
  AccountCreated,
  MoneyInvested,
  SentimentReceived
}
