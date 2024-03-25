package com.azki.bankingsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BANK_ACCOUNT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {

  public BankAccount(String accountNumber, String holderName, Double balance){
    this.accountNumber = accountNumber;
    this.holderName = holderName;
    this.balance = balance;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String accountNumber;
  private String holderName;
  private Double balance;



}
