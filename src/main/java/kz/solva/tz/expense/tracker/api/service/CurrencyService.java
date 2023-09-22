package kz.solva.tz.expense.tracker.api.service;

import kz.solva.tz.expense.tracker.api.data.CurrencyEntity;
import kz.solva.tz.expense.tracker.api.dto.enums.Currency;

import java.util.List;

public interface CurrencyService {
    List<CurrencyEntity> findAll();

    CurrencyEntity findCurrencyByShortname(Currency currency);
}
