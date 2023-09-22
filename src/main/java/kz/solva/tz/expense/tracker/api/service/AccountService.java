package kz.solva.tz.expense.tracker.api.service;

import kz.solva.tz.expense.tracker.api.data.Account;
import kz.solva.tz.expense.tracker.api.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void saveAll(List<Account> accounts){
        accountRepository.saveAll(accounts);
    }
}
