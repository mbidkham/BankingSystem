package com.azki.bankingsystem.service;

import com.azki.bankingsystem.dto.AccountCreationDto;
import com.azki.bankingsystem.model.BankAccount;
import com.azki.bankingsystem.repository.BankAccountRepository;
import com.azki.bankingsystem.utils.TransactionLogger;
import com.azki.bankingsystem.utils.TransactionObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankingManagementService {

  private final BankAccountRepository bankAccountRepository;
  private final List<TransactionObserver> observers = new ArrayList<>();
  private final ExecutorService executorService = Executors.newCachedThreadPool();
  public static final Map<Long, Lock> accountLocks = new ConcurrentHashMap<>();

  @Value(value = "${account.minimum.init.balance}")
  private Double initBalance;

  public BankingManagementService(BankAccountRepository bankAccountRepository,
      TransactionLogger transactionLogger) {
    this.bankAccountRepository = bankAccountRepository;
    addObserver(transactionLogger);
  }

  public void addObserver(TransactionObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(TransactionObserver observer) {
    observers.remove(observer);
  }

  /**
   * Logger implementation to notify and log
   *
   * @param accountNumber
   * @param transactionType = CREATE, DEPOSIT, WITHDRAWAL, TRANSFER_FROM, TRANSFER_TO
   * @param amount
   */
  private void notifyObservers(String accountNumber, String transactionType, double amount) {
    for (TransactionObserver observer : observers) {
      observer.onTransaction(accountNumber, transactionType, amount);
    }
  }

  public Long createAccount(AccountCreationDto accountCreationDto) {
    String accountNumber = UUID.randomUUID().toString();
    BankAccount account = new BankAccount(accountNumber, accountCreationDto.holderName(),
        initBalance);
    BankAccount createdBankAccount = bankAccountRepository.save(account);
    notifyObservers(accountNumber, "CREATE", initBalance);
    return createdBankAccount.getId();
  }


  //TODO:
  // Improve @Transactional methods to extract another method from this,
  // so the session won't be open until each thread completed
  @Transactional
  public String deposit(Long accountId, double amount) {
    BankAccount account = bankAccountRepository.findById(accountId)
        .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    executorService.submit(() -> {
      // Choose lock based on each user account id
      Lock accountLock = accountLocks.computeIfAbsent(accountId,
          k -> new ReentrantLock());
      try {
        accountLock.lock();
        account.setBalance(account.getBalance() + amount);
        bankAccountRepository.save(account);
        notifyObservers(account.getAccountNumber(), "DEPOSIT", amount);
      } finally {
        accountLock.unlock();
        accountLocks.remove(accountId);
      }
    });
    return account.getAccountNumber();
  }

  @Transactional
  public String withdraw(Long accountId, double amount) {
    BankAccount account = bankAccountRepository.findById(accountId)
        .orElseThrow(() -> new IllegalArgumentException("Account not found!"));
    executorService.submit(() -> {
          Lock accountLock = accountLocks.computeIfAbsent(accountId,
              k -> new ReentrantLock());
          try {
            accountLock.lock();
            if (account.getBalance() >= amount) {
              account.setBalance(account.getBalance() - amount);
              bankAccountRepository.save(account);
              notifyObservers(account.getAccountNumber(), "WITHDRAWAL", amount);
            } else {
              throw new IllegalArgumentException("Insufficient balance:(");
            }
          } finally {
            accountLock.unlock();
            accountLocks.remove(accountId);
          }
        }
    );
    return account.getAccountNumber();
  }


  @Transactional
  public void transfer(Long fromAccountId, Long toAccountId, double amount) {
    String fromAccountNum = withdraw(fromAccountId, amount);
    String toAccountNum = deposit(toAccountId, amount);
    notifyObservers(fromAccountNum, "TRANSFER_FROM", amount);
    notifyObservers(toAccountNum, "TRANSFER_TO", amount);
  }

  @Transactional(readOnly = true)
  public double getBalance(Long accountId) {
    synchronized (this) {
      BankAccount account = bankAccountRepository.findById(accountId)
          .orElseThrow(() -> new IllegalArgumentException("Account not found"));
      return account.getBalance();
    }
  }
}
