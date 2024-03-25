package com.azki.bankingsystem.component;

import com.azki.bankingsystem.service.BankingManagementService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImmediateTransferStrategy implements TransferStrategy{

  private final BankingManagementService bankingManagementService;
  @Override
  public void execute(Long fromAccountId, Long toAccountId, double amount) {
    bankingManagementService.transfer(fromAccountId, toAccountId, amount);
  }
}
