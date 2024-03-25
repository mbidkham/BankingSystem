package com.azki.bankingsystem.component;


public interface TransactionStrategy {
  void execute(Long accountId, double amount);

}
