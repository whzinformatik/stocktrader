package com.whz.account.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import com.whz.account.infrastructure.AccountData;

public interface AccountQueries {
  Completes<AccountData> accountOf(String id);
  Completes<Collection<AccountData>> accounts();
}