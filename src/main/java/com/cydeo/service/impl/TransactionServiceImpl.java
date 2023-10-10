package com.cydeo.service.impl;

import com.cydeo.exception.BadRequestException;
import com.cydeo.model.Account;
import com.cydeo.model.Transaction;
import com.cydeo.service.TransactionService;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TransactionServiceImpl implements TransactionService {

    @Override
    public Transaction makeTransfer(Account sender, Account receiver, BigDecimal amount, Data creationDate, String message) {

        /** If sender or receiver is null ?
         *  If sender and receiver is the same account ?
         *  If sender has enough balance to make transfer ?
         *  If both accounts are checking, if not, one of them saving, it needs to be same userId
         */

        validateAccount(sender,receiver);

        // makeTransfer


        return null;
    }

    private void validateAccount(Account sender, Account receiver) {
        /*
            -if any of the account is null
            -if account ids are the same(same account)
            -if the account exist in the database (repository)
         */

        if (sender == null || receiver == null ){
            throw new BadRequestException("Sender or Receiver cannot be null");
        }


    }

    @Override
    public List<Transaction> findAllTransaction() {
        return null;
    }
}
