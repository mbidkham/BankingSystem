package com.azki.bankingsystem.component;

import com.azki.bankingsystem.dto.AccountCreationDto;
import com.azki.bankingsystem.service.BankingManagementService;
import java.util.Scanner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleUi implements CommandLineRunner {

    private final BankingManagementService bankService;

    @Override
    public void run(String... args){
      start();
    }
    public ConsoleUi(BankingManagementService bankService) {
      this.bankService = bankService;
    }

    public void start() {
      Scanner scanner = new Scanner(System.in);
      while (true) {
        System.out.println("1. Create Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Check Balance");
        System.out.println("6. Exit");
        System.out.print("Choose option: ");
        int option = scanner.nextInt();
        switch (option) {
          case 1:
            createAccount(scanner);
            break;
          case 2:
            deposit(scanner);
            break;
          case 3:
            withdraw(scanner);
            break;
          case 4:
            transfer(scanner);
            break;
          case 5:
            getBalance(scanner);
            break;
          case 6:
            System.out.println("Exiting");
            return;
          default:
            System.out.println("Invalid option");
        }
      }
    }

    private void createAccount(Scanner scanner) {
      System.out.print("Enter account holder name: ");
      String accountHolderName = scanner.next();
      Long accountId = bankService.createAccount(
          AccountCreationDto
          .builder()
          .holderName(accountHolderName)
          .build());
      System.out.println("Account created with ID: " + accountId);
    }

    private void deposit(Scanner scanner) {
      System.out.print("Enter account ID: ");
      Long accountId = scanner.nextLong();
      System.out.print("Enter amount to deposit: ");
      double amount = scanner.nextDouble();
      processTransaction(accountId, amount, new DepositStrategy(bankService));
      System.out.println("Deposit successful");
    }

    private void withdraw(Scanner scanner) {
      System.out.print("Enter account ID: ");
      Long accountId = scanner.nextLong();
      System.out.print("Enter amount to withdraw: ");
      double amount = scanner.nextDouble();
      processTransaction(accountId, amount, new WithdrawalStrategy(bankService));
      System.out.println("Withdrawal successful");
    }

    private void transfer(Scanner scanner) {
      System.out.print("Enter from account ID: ");
      Long fromAccountId = scanner.nextLong();
      System.out.print("Enter to account ID: ");
      Long toAccountId = scanner.nextLong();
      System.out.print("Enter amount to transfer: ");
      double amount = scanner.nextDouble();
      processTransfer(fromAccountId, toAccountId, amount, new ImmediateTransferStrategy(bankService));
      System.out.println("Transfer successful");
    }

    private void getBalance(Scanner scanner) {
      System.out.print("Enter account ID: ");
      Long accountId = scanner.nextLong();
      double balance = bankService.getBalance(accountId);
      System.out.println("Account balance: " + balance);
    }

    private void processTransaction(Long accountId, Double amount, TransactionStrategy transactionStrategy){
      transactionStrategy.execute(accountId, amount);
    }
    private void processTransfer(Long fromAccountId, Long toAccountId,
        Double amount, TransferStrategy transferStrategy){
      transferStrategy.execute(fromAccountId, toAccountId, amount);
    }
}
