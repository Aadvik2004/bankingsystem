package com.aadvik.bankingsystem.controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.aadvik.bankingsystem.entity.Account;
import com.aadvik.bankingsystem.repository.AccountRepository;

@RestController
@RequestMapping("/accounts")

public class AccountController {

    private final AccountRepository repository;

    public AccountController(AccountRepository repository) {
        this.repository = repository;
    }
    
    

    @PostMapping
    public Account create(@RequestBody Account account) {
        return repository.save(account);
    }

    @GetMapping
    public List<Account> getAll() {
        return repository.findAll();
    }
    
    @PutMapping("/{id}/deposit")
    public Account deposit(@PathVariable Long id, @RequestParam double amount) {
        Account account = repository.findById(id).orElseThrow();
        account.setBalance(account.getBalance()+amount);
        return repository.save(account);
    }
    
    @PutMapping("/{id}/withdraw")
    public Account withdraw(@PathVariable Long id, @RequestParam double amount) {
        Account account = repository.findById(id).orElseThrow();

        if (amount>account.getBalance()) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance()-amount);
        return repository.save(account);
    }
    
    @Transactional
    @PutMapping("/transfer")
    public String transfer(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam double amount) {
        Account sender = repository.findById(fromId).orElseThrow();
        Account receiver = repository.findById(toId).orElseThrow();

        if (amount>sender.getBalance()) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance()-amount);
        receiver.setBalance(receiver.getBalance()+amount);

        repository.save(sender);
        repository.save(receiver);

        return "Transfer successful";
    }
}