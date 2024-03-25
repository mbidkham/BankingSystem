package com.azki.bankingsystem.component;

import com.azki.bankingsystem.service.BankingManagementService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WithdrawalStrategy implements TransactionStrategy{

  private final BankingManagementService bankingManagementService;
  @Override
  public void execute(Long accountId, double amount) {
    bankingManagementService.withdraw(accountId, amount);
  }

}
