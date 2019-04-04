package com.example.demo.controller;

import com.example.demo.domain.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("create")
    public Account create() {
        Account account = new Account();
        account.setEmail("rltjr219@gmail.com");
        account.setPassword("password");

        return accountService.save(account);
    }

}
