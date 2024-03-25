package com.azki.bankingsystem.component;


public interface TransferStrategy {
  void execute(Long fromAccountId, Long toAccountId,  double amount);

}
