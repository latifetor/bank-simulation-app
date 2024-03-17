package com.cydeo.service.impl;

import com.cydeo.dto.AccountDTO;
import com.cydeo.enums.AccountStatus;
import com.cydeo.enums.AccountType;
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
    public AccountDTO createNewAccount(BigDecimal balance, Date createDate, AccountType accountType, Long userId) {

        // create Account_object
        AccountDTO accountDTO = AccountDTO.builder().id(UUID.randomUUID()).userId(userId)
                .balance(balance).accountType(accountType).creationDate(createDate)
                .accountStatus(AccountStatus.ACTIVE).build();

        // save into the database(repository)
        // return the object created

        return accountRepository.save(accountDTO);
    }

    @Override
    public List<AccountDTO> ListAllAccount() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteAccount(Long id) {
        // find the account belongs to the id
        AccountDTO accountDTO = accountRepository.findById(id);
        // set status to deleted
        accountDTO.setAccountStatus(AccountStatus.DELETED);

    }

    @Override
    public void activateAccount(Long id) {

        // find the account belongs to the id
        AccountDTO accountDTO = accountRepository.findById(id);

        accountDTO.setAccountStatus(AccountStatus.ACTIVE);
    }

    @Override
    public AccountDTO retrieveById(Long id) {
        return accountRepository.findById(id);
    }
}
