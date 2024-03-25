package com.azki.bankingsystem.utils;

import java.io.FileWriter;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class TransactionLogger implements TransactionObserver{

  private static final String LOGS_FILE = "transactions.log";
  private final FileWriter fileWriter;

  public TransactionLogger() throws IOException {
    this.fileWriter = new FileWriter(LOGS_FILE, true);
  }

  @Override
  public synchronized void onTransaction(String accountNumber, String transactionType, double amount) {
    try {
      fileWriter.write("AccountNumber: "+ accountNumber + " ," +
          "TransactionType: " + transactionType + " ," + "Amount: "+ amount + "\n");
      fileWriter.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
