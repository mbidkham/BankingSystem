package com.azki.bankingsystem.service;


public interface TransactionStrategy {
  void execute(Long accountId, double amount);

}
