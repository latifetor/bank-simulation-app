package com.cydeo.service.impl;

import com.cydeo.dto.AccountDTO;
import com.cydeo.entity.Transaction;
import com.cydeo.enums.AccountType;
import com.cydeo.exception.AccountOwnershipException;
import com.cydeo.exception.BadRequestException;
import com.cydeo.exception.BalanceNotSufficientException;
import com.cydeo.exception.UnderConstructionException;
import com.cydeo.dto.TransactionDTO;
import com.cydeo.mapper.TransactionMapper;
import com.cydeo.repository.TransactionRepository;
import com.cydeo.service.AccountService;
import com.cydeo.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionServiceImpl implements TransactionService {

    @Value("${under_construction}")
    private boolean underConstruction;
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(AccountService accountService, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public TransactionDTO makeTransfer(AccountDTO sender, AccountDTO receiver, BigDecimal amount, Date creationDate, String message) {

        if(!underConstruction) {
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

            TransactionDTO transactionDTO = new TransactionDTO();

            // save into the DB and return it
            return transactionRepository.save(transactionDTO);
        }else {
            throw new UnderConstructionException("App is under construction, please try again later");
        }
    }

    private void executeBalanceAndUpdateIfRequired(BigDecimal amount, AccountDTO sender, AccountDTO receiver) {
        if(checkSenderBalance(sender,amount)) {
            // update sender and receiver balance
            sender.setBalance(sender.getBalance().subtract(amount));
            receiver.setBalance(receiver.getBalance().add(amount));
        }else {
            throw new BalanceNotSufficientException("Balance is not enough for this transfer");

        }
    }

    private boolean checkSenderBalance(AccountDTO sender, BigDecimal amount) {
        //verify sender has enough balance to send
        return sender.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) >=0;
    }

    // check the account ownership:
    private void checkAccountOwnership(AccountDTO sender, AccountDTO receiver) {
        /*
            write an if_statement that check if one of the account is saving,
            and user of sender or receiver in not the same, throw AccountOwnershipException
         */
        if ((sender.getAccountType().equals(AccountType.SAVING) || receiver.getAccountType().equals(AccountType.SAVING))
                && !sender.getUserId().equals(receiver.getUserId()) ){
            throw new AccountOwnershipException("If one of the account is saving, user must be the same for sender and receiver");
        }
    }

    private void validateAccount(AccountDTO sender, AccountDTO receiver) {
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
    private void findAccountById(Long id) {
        accountService.retrieveById(id);
    }

    @Override
    public List<TransactionDTO> findAllTransaction() {
        // get the transaction entity for all and return them as a list of TransactionDTO
        return transactionRepository.findAll().stream()
                .map(transactionMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> last10Transactions() {
        //we want last 10 latest transaction
        //write a query to get the result ofr last 10 transaction
        List<Transaction> last10Transactions = transactionRepository.findLast10Transactions();
        // convert to dto and return it
        return last10Transactions.stream().map(transactionMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> findTransactionListById(Long id) {
        // get the list of transactions if account_id is involved as a sender or receiver
        List<Transaction> transactionList = transactionRepository.findTransactionListByAccountId(id);
        // convert list of entity to dot and return it
        return transactionList.stream().map(transactionMapper::convertToDTO).collect(Collectors.toList());
    }
}
