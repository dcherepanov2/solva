package kz.solva.tz.expense.tracker.api.service;

import kz.solva.tz.expense.tracker.api.data.Account;
import kz.solva.tz.expense.tracker.api.data.ExpenseCategory;
import kz.solva.tz.expense.tracker.api.data.ExpenseLimit;
import kz.solva.tz.expense.tracker.api.dto.LimitReqest;
import kz.solva.tz.expense.tracker.api.exception.AccountNotFoundException;
import kz.solva.tz.expense.tracker.api.exception.TwelvedataApiException;

import java.math.BigDecimal;

public interface LimitService {
    ExpenseLimit setNewLimit(LimitReqest request) throws AccountNotFoundException;

    BigDecimal getRemainderByAccountForCurrencyTransaction(Account account, ExpenseCategory category) throws TwelvedataApiException;

    ExpenseLimit getLimitByCategory(Account account, ExpenseCategory category);
}
