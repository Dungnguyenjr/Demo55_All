package com.tn.service;

import com.tn.entity.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountService extends UserDetailsService {

    List<Account> getAll();

    boolean delete(int id);

    boolean save(Account account);
}
