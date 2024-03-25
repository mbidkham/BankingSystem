package com.azki.bankingsystem.component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class AccountLockManager {
  public static final Map<Long, Lock> accountLocks = new ConcurrentHashMap<>();


}
