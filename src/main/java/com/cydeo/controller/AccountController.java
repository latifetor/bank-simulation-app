package com.cydeo.controller;

import com.cydeo.enums.AccountType;
import com.cydeo.model.Account;
import com.cydeo.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class AccountController {

    // injecting the service_interface
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // write a method to return index.html including account list information endpoint:index
    @GetMapping("/index")
    public String getIndexPage(Model model){

        model.addAttribute("accountList",accountService.ListAllAccount());


       return "account/index";
    }

    @GetMapping("/create-form")
    public String getCreateForm(Model model){

        //we need to provide empty account object
        model.addAttribute("account", Account.builder().build());

        //we need to provide accountType enum info for filling the dropdown options
        model.addAttribute("accountTypes", AccountType.values());

        return "account/create-account";
    }

    // create a method to capture information from UI
    // print them on the console: to see what user entering
    // trigger createNewAccount method, create the account based the user input
    // once user created then return back to the index.page

    @PostMapping("/create")
    public String createAccount(@ModelAttribute("account") Account account){
        System.out.println(account);

        accountService.createNewAccount(account.getBalance(),new Date(),account.getAccountType(),account.getUserId());

        return "redirect:/index";
    }



}
