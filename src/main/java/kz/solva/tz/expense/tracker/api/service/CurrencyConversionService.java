package kz.solva.tz.expense.tracker.api.service;

import kz.solva.tz.expense.tracker.api.data.CurrencyConversion;
import kz.solva.tz.expense.tracker.api.data.CurrencyExchangeRateEntity;
import kz.solva.tz.expense.tracker.api.data.Transaction;
import kz.solva.tz.expense.tracker.api.exception.TwelvedataApiException;

import java.util.List;

public interface CurrencyConversionService {
    List<CurrencyConversion> saveAllConversionForCurrencyAll(Transaction transaction) throws TwelvedataApiException;

    CurrencyConversion saveCurrencyConversion(CurrencyExchangeRateEntity currencyExchange, Transaction transaction) throws TwelvedataApiException;
}
