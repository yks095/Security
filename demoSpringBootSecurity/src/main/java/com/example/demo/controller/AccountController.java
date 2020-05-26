package com.example.demo.controller;

import com.example.demo.domain.Account;
import com.example.demo.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("create")
    public Account create() {
        Account account = new Account();
        account.setEmail("rltjr219@gmail.com");
        account.setPassword("password");

        return accountService.save(account);
    }

}
