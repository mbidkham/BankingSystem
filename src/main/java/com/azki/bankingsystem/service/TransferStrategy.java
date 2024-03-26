package com.azki.bankingsystem.service;


public interface TransferStrategy {
  void execute(Long fromAccountId, Long toAccountId,  double amount);

}
