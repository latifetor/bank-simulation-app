package com.cydeo.service.impl;

import com.cydeo.enums.AccountStatus;
import com.cydeo.enums.AccountType;
import com.cydeo.model.Account;
import com.cydeo.repository.AccountRepository;
import com.cydeo.service.AccountService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class AccountServiceImpl implements AccountService {

    // inject the repository
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createNewAccount(BigDecimal balance, Date createDate, AccountType accountType, Long userId) {

        // create Account_object
        Account account = Account.builder().id(UUID.randomUUID()).userId(userId)
                .balance(balance).accountType(accountType).creationDate(createDate)
                .accountStatus(AccountStatus.ACTIVE).build();

        // save into the database(repository)
        // return the object created

        return accountRepository.save(account);
    }

    @Override
    public List<Account> ListAllAccount() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteAccount(UUID id) {
        // find the account belongs to the id
        Account account = accountRepository.findById(id);
        // set status to deleted
        account.setAccountStatus(AccountStatus.DELETED);

    }

    @Override
    public void activateAccount(UUID id) {

        // find the account belongs to the id
        Account account = accountRepository.findById(id);

        account.setAccountStatus(AccountStatus.ACTIVE);
    }

    @Override
    public Account retrieveById(UUID id) {
        return accountRepository.findById(id);
    }
}
