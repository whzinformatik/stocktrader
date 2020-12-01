package com.whz.account.infrastructure.persistence;

import com.whz.account.infrastructure.AccountData;
import io.vlingo.common.Completes;
import java.util.Collection;

public interface AccountQueries {
  Completes<AccountData> accountOf(String id);

  Completes<Collection<AccountData>> accounts();
}
