package com.cydeo.service.impl;

import com.cydeo.enums.AccountType;
import com.cydeo.exception.AccountOwnershipException;
import com.cydeo.exception.BadRequestException;
import com.cydeo.exception.BalanceNotSufficientException;
import com.cydeo.model.Account;
import com.cydeo.model.Transaction;
import com.cydeo.repository.AccountRepository;
import com.cydeo.repository.TransactionRepository;
import com.cydeo.service.TransactionService;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;

    }

    @Override
    public Transaction makeTransfer(Account sender, Account receiver, BigDecimal amount, Date creationDate, String message) {

        /** If sender or receiver is null ?
         *  If sender and receiver is the same account ?
         *  If sender has enough balance to make transfer ?
         *  If both accounts are checking, if not, one of them saving, it needs to be same userId
         */

        validateAccount(sender,receiver);
        checkAccountOwnership(sender,receiver);
        executeBalanceAndUpdateIfRequired(amount,sender,receiver);

        /**
         * after all validations are completed, and money is transferred
         * we need to create Transaction object and save/return it
         */

        Transaction transaction = Transaction.builder().amount(amount).sender(sender.getId()).receiver(receiver.getId())
                .createDate(creationDate).message(message).build();

        // save into the DB and return it
        return transactionRepository.save(transaction);
    }

    private void executeBalanceAndUpdateIfRequired(BigDecimal amount, Account sender, Account receiver) {
        if(checkSenderBalance(sender,amount)) {
            // update sender and receiver balance
            sender.setBalance(sender.getBalance().subtract(amount));
            receiver.setBalance(receiver.getBalance().add(amount));
        }else {
            throw new BalanceNotSufficientException("Balance is not enough for this transfer");

        }
    }

    private boolean checkSenderBalance(Account sender, BigDecimal amount) {
        //verify sender has enough balance to send
        return sender.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) >=0;
    }

    // check the account ownership:
    private void checkAccountOwnership(Account sender, Account receiver) {
        /*
            write an if_statement that check if one of the account is saving,
            and user of sender or receiver in not the same, throw AccountOwnershipException
         */
        if ((sender.getAccountType().equals(AccountType.SAVING) || receiver.getAccountType().equals(AccountType.SAVING))
                && !sender.getUserId().equals(receiver.getUserId()) ){
            throw new AccountOwnershipException("If one of the account is saving, user must be the same for sender and receiver");
        }
    }

    private void validateAccount(Account sender, Account receiver) {
        /*
            -if any of the account is null
            -if account ids are the same(same account)
            -if the account exist in the database (repository)
         */

        // if any of the account is null
        if (sender == null || receiver == null ){
            throw new BadRequestException("Sender or Receiver cannot be null");
        }

        // if accounts are the same throw BadRequestException with message: "account needs to be different
        if (sender.getId().equals(receiver.getId())){
            throw new BadRequestException("Sender account needs to be different than receiver account");
        }

        // if the account exist in the database (repository)
        findAccountById(sender.getId());
        findAccountById(receiver.getId());
    }
    private void findAccountById(UUID id) {
        accountRepository.findById(id);
    }


    @Override
    public List<Transaction> findAllTransaction() {
        return transactionRepository.findAll();
    }
}
