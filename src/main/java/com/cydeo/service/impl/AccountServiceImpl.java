package com.cydeo.service.impl;

import com.cydeo.dto.AccountDTO;
import com.cydeo.entity.Account;
import com.cydeo.enums.AccountStatus;
import com.cydeo.mapper.AccountMapper;
import com.cydeo.repository.AccountRepository;
import com.cydeo.service.AccountService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountServiceImpl implements AccountService {

    // inject the repository
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public void  createNewAccount(AccountDTO accountDTO) {

        accountDTO.setCreationDate(new Date());
        accountDTO.setAccountStatus(AccountStatus.ACTIVE);
        //save into the database(repository)
        accountRepository.save(accountMapper.convertToEntity(accountDTO));
    }

    @Override
    public List<AccountDTO> listAllAccount() {
        //we are getting list of account and we need to return list of AccountDTO
        List<Account> accountList = accountRepository.findAll();
        //we are converting entity to dto list and return it
        return accountList.stream().map(accountMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {
        //find the account object based on id
        Account account = accountRepository.findById(id).get();
        //set status to deleted
        account.setAccountStatus(AccountStatus.DELETED);
        //save the updated account object
        accountRepository.save(account);
    }

    @Override
    public void activateAccount(Long id) {
        //find the account belongs the id
        Account account = accountRepository.findById(id).get();
        //set status to active
        account.setAccountStatus(AccountStatus.ACTIVE);
        //save the updated account object
        accountRepository.save(account);
    }

    @Override
    public AccountDTO retrieveById(Long id) {
        //find the account entity based on id, then convert it dto and return it
        return accountMapper.convertToDTO(accountRepository.findById(id).get());
    }

    @Override
    public List<AccountDTO> listAllActiveAccount() {
        // need list of active accounts from repository
        List<Account> accountList = accountRepository.findAllByAccountStatus(AccountStatus.ACTIVE);

        // convert active accounts to accountDto and return it
        return accountList.stream().map(accountMapper::convertToDTO).collect(Collectors.toList());

//        return accountRepository.findAllByAccountStatus(AccountStatus.ACTIVE).stream()
//                .map(accountMapper::convertToDTO).collect(Collectors.toList());
    }
}
