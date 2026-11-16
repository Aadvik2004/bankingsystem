package com.aadvik.bankingsystem.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    
    
    public ResponseEntity<?> deposit(@PathVariable Long id, @RequestParam double amount) {
    	
    	if (amount <= 0) {
            return ResponseEntity.badRequest().body("Amount must be positive");
        }
    	
        Account account = repository.findById(id).orElseThrow();
        account.setBalance(account.getBalance()+amount);
        
        return ResponseEntity.ok(repository.save(account));
        
    }
    
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable Long id, @RequestParam double amount) {
        Account account = repository.findById(id).orElseThrow();
        
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("Amount must be positive");
        }

        if (amount > account.getBalance()) {
            return ResponseEntity.badRequest().body("Insufficient balance");
            
        }

        account.setBalance(account.getBalance() - amount);
        return ResponseEntity.ok(repository.save(account));
    }
    
    @Transactional
    @PutMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam double amount) {
    	
    	if (amount <= 0) {
            return ResponseEntity.badRequest().body("Amount must be positive");      
    	}
    	
    	Account sender = repository.findById(fromId).orElseThrow();
        Account receiver = repository.findById(toId).orElseThrow();

        if (amount>sender.getBalance()) {
        	return ResponseEntity.badRequest().body("Insufficient balance");
        }

        sender.setBalance(sender.getBalance()-amount);
        receiver.setBalance(receiver.getBalance()+amount);

        repository.save(sender);
        repository.save(receiver);

        return ResponseEntity.ok("Transfer successful");    }
}