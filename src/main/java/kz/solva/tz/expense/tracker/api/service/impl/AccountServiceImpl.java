package kz.solva.tz.expense.tracker.api.service.impl;

import kz.solva.tz.expense.tracker.api.data.Account;
import kz.solva.tz.expense.tracker.api.repository.AccountRepository;
import kz.solva.tz.expense.tracker.api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("accountServiceImpl")
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void saveAll(List<Account> accounts){
        accountRepository.saveAll(accounts);
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByAccountNumber(number);
    }
}
