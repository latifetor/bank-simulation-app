package com.cydeo.controller;

import com.cydeo.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

}
