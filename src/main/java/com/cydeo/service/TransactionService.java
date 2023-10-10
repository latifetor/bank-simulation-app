package com.cydeo.service;

import com.cydeo.model.Account;
import com.cydeo.model.Transaction;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    Transaction makeTransfer(Account sender, Account receiver, BigDecimal amount, Data creationDate, String message);

    List<Transaction> findAllTransaction();

}
