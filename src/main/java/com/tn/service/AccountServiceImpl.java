package com.tn.service;

import com.tn.entity.Account;
import com.tn.repository.AccountRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepo;

    public AccountServiceImpl(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = accountRepo.findAll();

        return accounts;
    }

    @Override
    public boolean delete(int id) {
        accountRepo.deleteById(id);
        return true;
    }

    @Override
    public boolean save(Account account) {
        accountRepo.save(account);
        return true;
    }

    @Override
    public Account getById(int id) {
        Account account = accountRepo.findById(id).orElse(null);
        return account;
    }

    @Override
    public List<Account> search(String data) {
        List<Account> accounts = accountRepo.searchData(data);
        return accounts;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepo.findByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        return new User(username, account.getPassword(), AuthorityUtils.createAuthorityList("ROLE_" + account.getRole()));
    }
}
