package com.cydeo.controller;

import com.cydeo.model.Account;
import com.cydeo.model.Transaction;
import com.cydeo.service.AccountService;
import com.cydeo.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class TransactionController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public TransactionController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }


    @GetMapping("/make-transfer")
    public String getMakeTransfer(Model model){

        //what we need to provide to make transfer happen
        //we need to provide empty transaction object
        model.addAttribute("transaction", Transaction.builder().build());
        //we need to provide list of all accounts
        model.addAttribute("accounts",accountService.ListAllAccount());
        //we need list of last 10 transactions to fill the table(business logic is missing)
        model.addAttribute("lastTransactions",transactionService.last10Transactions());

        return "transaction/make-transfer";
    }

    @PostMapping("/transfer")
    // write a post method that takes transaction object from the UI
    // complete the transfer and return the same page
    public String makeTransfer(@ModelAttribute("transaction") Transaction transaction){

        // having UUID of accounts but need to provide Account_object to the method
        // need to find the Accounts based on the ID that we have and use as a parameter to complete makeTransfer method.
        Account sender = accountService.retrieveById(transaction.getSender());
        Account receiver = accountService.retrieveById(transaction.getReceiver());

        transactionService.makeTransfer(sender,receiver,transaction.getAmount(), new Date(),transaction.getMessage());

        return "redirect:/make-transfer";
    }

}
