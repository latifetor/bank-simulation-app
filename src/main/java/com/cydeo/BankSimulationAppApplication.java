package com.cydeo;

import com.cydeo.dto.AccountDTO;
import com.cydeo.enums.AccountType;
import com.cydeo.service.AccountService;
import com.cydeo.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootApplication
public class BankSimulationAppApplication {

    public static void main(String[] args) {
        ApplicationContext container = SpringApplication.run(BankSimulationAppApplication.class, args);

        // 1st step: get account and transaction service beans
        AccountService accountService = container.getBean(AccountService.class);
        TransactionService transactionService = container.getBean(TransactionService.class);

//        // 2nd step: create sender and receiver accounts using AccountService
//        AccountDTO sender = accountService.createNewAccount(BigDecimal.valueOf(70), new Date(), AccountType.CHECKING, 1L);
//        AccountDTO receiver = accountService.createNewAccount(BigDecimal.valueOf(50), new Date(), AccountType.CHECKING, 2L);
//        AccountDTO receiver2 = accountService.createNewAccount(BigDecimal.valueOf(5000), new Date(), AccountType.CHECKING, 123L);
//        AccountDTO receiver3 = accountService.createNewAccount(BigDecimal.valueOf(7500), new Date(), AccountType.SAVING, 124L);

//        // see all available accounts
//        accountService.ListAllAccount().forEach(System.out::println);
//
//        // testing the transaction: the main functionality makeTransfer_method
//        transactionService.makeTransfer(sender,receiver,new BigDecimal(40),new Date(),"Transaction 1");
//        System.out.println(transactionService.findAllTransaction().get(0));
//
//        // see all available accounts
//        accountService.ListAllAccount().forEach(System.out::println);

    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
