package kz.solva.tz.expense.tracker.api.service;

import kz.solva.tz.expense.tracker.api.data.CurrencyExchangeRateEntity;
import kz.solva.tz.expense.tracker.api.data.ExpenseLimit;
import kz.solva.tz.expense.tracker.api.data.LimitExceeded;

public interface LimitExceededService {
    LimitExceeded createNewLimitedExceed(CurrencyExchangeRateEntity currencyExchange, Boolean limitFlag, ExpenseLimit accountLimit);
}
