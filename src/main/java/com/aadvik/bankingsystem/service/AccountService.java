package com.aadvik.bankingsystem.service;

import org.springframework.stereotype.Service;

import com.aadvik.bankingsystem.repository.AccountRepository;

@Service
public class AccountService {
	 private final AccountRepository repository;

	    public AccountService(AccountRepository repository) {
	        this.repository = repository;
	    }
}