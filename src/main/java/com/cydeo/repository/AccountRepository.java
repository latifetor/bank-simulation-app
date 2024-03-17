package com.cydeo.repository;

import com.cydeo.dto.AccountDTO;
import com.cydeo.exception.RecordNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Component
public class AccountRepository {

    public static List<AccountDTO> accountDTOList = new ArrayList<>();

    public AccountDTO save(AccountDTO accountDTO){
        accountDTOList.add(accountDTO);
        return accountDTO;
    }

    public List<AccountDTO> findAll() {
        return accountDTOList;
    }

    public AccountDTO findById(UUID id) {
        /*TASK:
            - write the method that find the account inside the List
            - if not: throw RecordNotFoundException
         */

        return accountDTOList.stream()
                .filter(account -> account.getId().equals(id))
                .findAny().orElseThrow(()-> new RecordNotFoundException("Account does not in the database"));
    }






}
