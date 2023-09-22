package kz.solva.tz.expense.tracker.api.service;

import kz.solva.tz.expense.tracker.api.data.CurrencyEntity;
import kz.solva.tz.expense.tracker.api.data.CurrencyExchangeRateEntity;
import kz.solva.tz.expense.tracker.api.dto.enums.Currency;
import kz.solva.tz.expense.tracker.api.exception.TwelvedataApiException;
import kz.solva.tz.expense.tracker.api.integration.fixer.api.dto.QuoteResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CurrencyRateTrackerService{
    QuoteResponse fetchCurrencyPairData(Currency base, Currency to);

    CurrencyExchangeRateEntity getCurrencyExchangeRate(Currency base, Currency to, LocalDate date) throws TwelvedataApiException;

    BigDecimal convertCurrencyAmount(Currency from, Currency to, BigDecimal amount) throws TwelvedataApiException;

    CurrencyExchangeRateEntity saveCurrencyExchange(QuoteResponse response, CurrencyEntity baseEntity, CurrencyEntity toEntity, LocalDate date);
}
