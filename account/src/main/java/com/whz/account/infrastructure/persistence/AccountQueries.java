/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.infrastructure.persistence;

import com.whz.account.infrastructure.AccountData;
import io.vlingo.common.Completes;
import java.util.Collection;

/** @since 1.0.0 */
public interface AccountQueries {
  /** @since 1.0.0 */
  Completes<AccountData> accountOf(String id);

  /** @since 1.0.0 */
  Completes<Collection<AccountData>> accounts();
}
