package kz.solva.tz.expense.tracker.api.service;

import kz.solva.tz.expense.tracker.api.data.Account;

import java.util.List;

public interface AccountService {
    void saveAll(List<Account> accounts);

    Account findByNumber(String number);

}
