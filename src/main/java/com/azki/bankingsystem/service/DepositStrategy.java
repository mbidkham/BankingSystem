package com.azki.bankingsystem.service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DepositStrategy implements TransactionStrategy{

  private final BankingManagementService bankingManagementService;

  @Override
  public void execute(Long accountId, double amount) {
    bankingManagementService.deposit(accountId, amount);
  }
}
