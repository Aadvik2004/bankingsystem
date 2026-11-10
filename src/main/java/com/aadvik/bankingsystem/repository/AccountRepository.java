package com.aadvik.bankingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aadvik.bankingsystem.entity.Account;

public interface AccountRepository extends JpaRepository<Account,Long> {

}
