package com.azki.bankingsystem.utils;

public interface TransactionObserver {
  void onTransaction(String accountNumber, String transactionType, double amount);

}
